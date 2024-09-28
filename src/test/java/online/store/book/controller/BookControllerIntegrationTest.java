package online.store.book.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import online.store.book.dto.book.BookDto;
import online.store.book.dto.book.BookSearchParameters;
import online.store.book.dto.book.CreateBookRequestDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.ErrorResponse;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerIntegrationTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) throws Exception {
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("db/books/add_books_for_Integration_Test.sql"));
        }
    }

    @AfterEach
    void tearDown(@Autowired DataSource dataSource) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("db/books/remove_books_for_Integration_Test.sql"));
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("George Orwell");
        bookRequestDto.setTitle("Animal Farm: A Fairy Story");
        bookRequestDto.setIsbn("123");
        bookRequestDto.setPrice(new BigDecimal(10));
        bookRequestDto.setCategoryIds(new ArrayList<>());

        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setAuthor(bookRequestDto.getAuthor());
        expectedBookDto.setTitle(bookRequestDto.getTitle());
        expectedBookDto.setIsbn(bookRequestDto.getIsbn());
        expectedBookDto.setPrice(bookRequestDto.getPrice());
        expectedBookDto.setCategoryIds(bookRequestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actualBookDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        assertNotNull(actualBookDto);
        EqualsBuilder.reflectionEquals(expectedBookDto, actualBookDto, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createBook_InValidRequestDto_NotSuccess() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("       ");
        bookRequestDto.setTitle("Animal Farm: A Fairy Story");
        bookRequestDto.setIsbn("123");
        bookRequestDto.setPrice(new BigDecimal(10));
        bookRequestDto.setCategoryIds(new ArrayList<>());

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertFalse(responseBody.isEmpty());
        assertTrue(responseBody.contains("author must not be blank"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAll_ReturnCorrectBooks() throws Exception {
        MvcResult result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> bookDtoList = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<List<BookDto>>() {
        });
        assertFalse(bookDtoList.isEmpty());
        assertEquals(3, bookDtoList.size());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getBookById_ExistingId_ShouldReturnCorrectBook() throws Exception {
        BookDto expectedBook = new BookDto();
        expectedBook.setId(2L);
        expectedBook.setTitle("Horror Stories");
        expectedBook.setAuthor("Jane Smith");
        expectedBook.setIsbn("978-0987654321");
        expectedBook.setPrice(new BigDecimal("14.99"));
        expectedBook.setCategoryIds(List.of(2L));

        MvcResult result = mockMvc.perform(get("/books/2"))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actualBookDto = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actualBookDto);
        EqualsBuilder.reflectionEquals(expectedBook, actualBookDto);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getBookById_NotExistingId_NotSuccess() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/5"))
                .andExpect(status().isNotFound())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        Map<String, Object> responseBody = objectMapper.readValue(jsonResponse,
                new TypeReference<Map<String, Object>>() {});
        assertNotNull(responseBody.get("errors"));
        assertEquals("Can't find book with id: 5", responseBody.get("errors"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void search_ValidParameters_Success() throws Exception {
        BookDto expectedBook = new BookDto();
        expectedBook.setId(2L);
        expectedBook.setTitle("Horror Stories");
        expectedBook.setAuthor("Jane Smith");
        expectedBook.setIsbn("978-0987654321");
        expectedBook.setPrice(new BigDecimal("14.99"));
        expectedBook.setCategoryIds(List.of(2L));

        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("titles", "")
                        .param("authors", "Jane Smith")
                        .param("sort", "id"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<BookDto> bookDtoList = objectMapper.readValue(contentAsString, new TypeReference<List<BookDto>>() {
        });
        assertFalse(bookDtoList.isEmpty());
        assertEquals(2, bookDtoList.size());
        EqualsBuilder.reflectionEquals(expectedBook, bookDtoList.get(0),"id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateBook_ExistingID_shouldReturnCorrectBook() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("George Orwell");
        bookRequestDto.setTitle("Animal Farm: A Fairy Story");
        bookRequestDto.setIsbn("123555222999dsd");
        bookRequestDto.setPrice(new BigDecimal(10));
        bookRequestDto.setCategoryIds(List.of(2L));

        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setId(1L);
        expectedBookDto.setAuthor(bookRequestDto.getAuthor());
        expectedBookDto.setTitle(bookRequestDto.getTitle());
        expectedBookDto.setIsbn(bookRequestDto.getIsbn());
        expectedBookDto.setPrice(bookRequestDto.getPrice());
        expectedBookDto.setCategoryIds(bookRequestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult result = mockMvc.perform(put("/books/1")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        BookDto actualBookDto = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actualBookDto);
        EqualsBuilder.reflectionEquals(expectedBookDto, actualBookDto);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateBook_NotExistingID_NotSuccess() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("George Orwell");
        bookRequestDto.setTitle("Animal Farm: A Fairy Story");
        bookRequestDto.setIsbn("123");
        bookRequestDto.setPrice(new BigDecimal(10));
        bookRequestDto.setCategoryIds(List.of(2L));

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);
        long id = 50L;

        MvcResult result = mockMvc.perform(put("/books/" + id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        Map<String, Object> responseBody = objectMapper.readValue(jsonResponse,
                new TypeReference<Map<String, Object>>() {});
        assertNotNull(responseBody.get("errors"));
        assertEquals("Can't find book with id: " + id, responseBody.get("errors"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBook_ExistingID_Success() throws Exception {
        MvcResult result = mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
