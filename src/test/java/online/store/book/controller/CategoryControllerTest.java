package online.store.book.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import online.store.book.dto.book.BookDtoWithoutCategoryIds;
import online.store.book.dto.category.CategoryDto;
import online.store.book.dto.category.CreateCategoryRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final String URL_TEMPLATE = "/categories";
    private static final String SEPARATOR = "/";
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
    void setUp(@Autowired DataSource dataSource, TestInfo testInfo) throws Exception {
        if (testInfo.getTags().contains("NoSetUp")) {
            return;
        }
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("db/categories/add_categories_to_db.sql"));
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
                    new ClassPathResource("db/categories/remove_categories_from_db.sql"));
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts =
            "classpath:db/categories/remove_category_comedy.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Save category with valid parameters to db")
    void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto("comedy", "");
        CategoryDto expectedCategoryDto = new CategoryDto(3L, categoryRequestDto.name(),
                categoryRequestDto.description());
        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        CategoryDto actualCategoryDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        EqualsBuilder.reflectionEquals(expectedCategoryDto, actualCategoryDto, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Try to save category wit invalid parameters. Must return status 400")
    void createCategory_InValidRequestDto_NotSuccess() throws Exception {
        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto("  ", "");
        CategoryDto expectedCategoryDto = new CategoryDto(3L, categoryRequestDto.name(),
                categoryRequestDto.description());
        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(URL_TEMPLATE)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        assertFalse(responseBody.isEmpty());
        assertTrue(responseBody.contains("name must not be blank"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update category with existing id and valid parameters")
    void updateCategory_ValidRequestDto_Success() throws Exception {
        Long id = 2L;
        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto("comedy", "");
        CategoryDto expectedCategoryDto = new CategoryDto(id, categoryRequestDto.name(),
                categoryRequestDto.description());
        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL_TEMPLATE + SEPARATOR + id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actualCategoryDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);
        EqualsBuilder.reflectionEquals(expectedCategoryDto, actualCategoryDto, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update category with not existing id and valid parameters")
    void updateCategory_NotExistingId_NotSuccess() throws Exception {
        long id = 40L;
        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto("comedy", "");
        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL_TEMPLATE + SEPARATOR + id)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
        String responseString = result.getResponse().getContentAsString();

        assertTrue(responseString.contains("Can't find category with id: " + id));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete category with existing id")
    void deleteCategory_ExistingId_Success() throws Exception {
        long id = 1L;
        MvcResult result = mockMvc.perform(delete(URL_TEMPLATE + SEPARATOR + id))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Find all categories")
    void getAll_Success() throws Exception {
        MvcResult result = mockMvc.perform(get(URL_TEMPLATE))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryDto> categoryDtoList = objectMapper.readValue(result.getResponse()
                .getContentAsString(), new TypeReference<List<CategoryDto>>() {});
        assertFalse(categoryDtoList.isEmpty());
        assertEquals(2, categoryDtoList.size());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Find a category with an existing id")
    void getCategoryById_ExistingId_ShouldReturnCorrectCategory() throws Exception {
        Long id = 1L;
        CategoryDto expectedCategoryDto = new CategoryDto(id, "Science", null);

        MvcResult result = mockMvc.perform(get(URL_TEMPLATE + SEPARATOR + id))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actualCategoryDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        EqualsBuilder.reflectionEquals(expectedCategoryDto, actualCategoryDto, "description");
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Find a category with a not existing id")
    void getCategoryById_NotExistingId_ShouldException() throws Exception {
        long id = 20L;

        MvcResult result = mockMvc.perform(get(URL_TEMPLATE + SEPARATOR + id))
                .andExpect(status().isNotFound())
                .andReturn();
        String responseString = result.getResponse().getContentAsString();

        assertTrue(responseString.contains("Can't find category with id: " + id));
    }

    @Sql(scripts = {
            "classpath:db/categories/add_categories_to_db.sql",
            "classpath:db/books/add_books_with_categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/books/remove_books_from_db.sql",
            "classpath:db/categories/remove_categories_from_db.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    @DisplayName("Find a books by an existing category id")
    @WithMockUser(username = "user", roles = {"USER"})
    @Tag("NoSetUp")
    void getBooksByCategoryId_ExistingId_ShouldReturnCorrectBooks() throws Exception {
        long id = 1L;
        MvcResult result = mockMvc.perform(get(
                        URL_TEMPLATE + SEPARATOR + id + SEPARATOR + "books"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<BookDtoWithoutCategoryIds> bookDtoWithoutCategoryIds = objectMapper.readValue(
                contentAsString,
                new TypeReference<List<BookDtoWithoutCategoryIds>>() {
                });
        assertEquals(1, bookDtoWithoutCategoryIds.size());
        assertEquals("John Doe", bookDtoWithoutCategoryIds.get(0).author());
    }
}
