package online.store.book.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import online.store.book.dto.book.BookDto;
import online.store.book.dto.book.BookDtoWithoutCategoryIds;
import online.store.book.dto.book.BookSearchParameters;
import online.store.book.dto.book.CreateBookRequestDto;
import online.store.book.exceptions.EntityNotFoundException;
import online.store.book.mapper.BookMapper;
import online.store.book.model.Book;
import online.store.book.model.Category;
import online.store.book.repository.book.BookRepository;
import online.store.book.repository.book.BookSpecificationBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder specificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save correct book")
    void save_correctBook_shouldReturnCorrectBook() {
        CreateBookRequestDto bookRequestDto = createBookRequestDto();
        Book book = createBook();
        BookDto bookDto = createBookDto(book);

        Mockito.when(bookMapper.toEntity(bookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);
        Mockito.when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.save(bookRequestDto);

        assertEquals(bookDto, actual);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toEntity(bookRequestDto);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookMapper, bookRepository);

    }

    @Test
    @DisplayName("Find all books")
    void findAll_shouldReturnCorrectNumberOfBooks() {
        Pageable pageable = createPageable();
        Book book = createBook();
        BookDto bookDto = createBookDto(book);
        List<Book> bookList = List.of(book);
        Page<Book> bookPage = new PageImpl<>(bookList);
        List<BookDto> bookDtoList = List.of(bookDto);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(bookPage)).thenReturn(bookDtoList);

        List<BookDto> actual = bookService.findAll(pageable);

        assertEquals(1, actual.size());
        assertEquals(bookDto, actual.get(0));
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(bookPage);
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("Find a book by an existing id")
    void findBookById_existingId_shouldReturnCorrectBook() {
        Book book = createBook();
        BookDto bookDto = createBookDto(book);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actualBook = bookService.findBookById(anyLong());

        assertEquals(bookDto, actualBook);
        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Try to find a book by a not existing id")
    void findBookById_notExistingId_shouldThrowException() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findBookById(id));

        assertEquals("Can't find book with id: " + id, exception.getMessage());
        verify(bookRepository, times(1)).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Update a book by an existing is")
    void update_existingId_shouldReturnCorrectBook() {
        Long id = 1L;
        CreateBookRequestDto bookRequestDto = createBookRequestDto();
        Book book = createBook();
        BookDto bookDto = createBookDto(book);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        doNothing().when(bookMapper).updateBookFromDto(bookRequestDto, book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actualBookDto = bookService.update(id, bookRequestDto);

        assertEquals(bookDto, actualBookDto);
        verify(bookRepository, times(1)).findById(id);
        verify(bookMapper, times(1)).updateBookFromDto(bookRequestDto, book);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookMapper, bookRepository);
    }

    @Test
    @DisplayName("Try to update book by a not existing id")
    void update_notExistingId_shouldThrowException() {
        Long id = 10L;
        CreateBookRequestDto bookRequestDto = createBookRequestDto();

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.update(id, bookRequestDto));
        assertEquals("Can't find book with id: " + id, exception.getMessage());
        verify(bookRepository, times(1)).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Delete book by an existing id")
    void delete_testSuccess() {
        Long id = 1L;

        doNothing().when(bookRepository).deleteById(id);

        bookService.delete(id);
        verify(bookRepository).deleteById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Find a book using an existing search parameters")
    void search_existsParams_shouldReturnCorrectBooksList() {
        Pageable pageable = createPageable();
        Book book = createBook();
        BookDto bookDto = createBookDto(book);
        BookSearchParameters searchParams =
                new BookSearchParameters(new String[]{"Animal Farm: A Fairy Story"},
                        new String[]{"George Orwell"});
        Specification<Book> bookSpec = mock(Specification.class);
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        List<BookDto> bookDtos = List.of(bookDto);

        when(specificationBuilder.build(searchParams)).thenReturn(bookSpec);
        when(bookRepository.findAll(bookSpec, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(bookPage)).thenReturn(bookDtos);

        List<BookDto> result = bookService.search(searchParams, pageable);
        assertEquals(bookDtos, result);
        verify(specificationBuilder, times(1)).build(searchParams);
        verify(bookRepository, times(1)).findAll(bookSpec, pageable);
        verify(bookMapper, times(1)).toDto(bookPage);
        verifyNoMoreInteractions(bookRepository, specificationBuilder, bookMapper);
    }

    @Test
    @DisplayName("Find a book using a not search parameters")
    void search_notExistsParams_shouldReturnEmptyBooksList() {
        Pageable pageable = createPageable();
        BookSearchParameters searchParams =
                new BookSearchParameters(new String[]{"Book"},
                        new String[]{"Author"});
        Specification<Book> bookSpec = mock(Specification.class);
        Page<Book> bookPage = new PageImpl<>(new ArrayList<>());
        List<BookDto> bookDtos = new ArrayList<>();

        when(specificationBuilder.build(searchParams)).thenReturn(bookSpec);
        when(bookRepository.findAll(bookSpec, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(bookPage)).thenReturn(bookDtos);

        List<BookDto> result = bookService.search(searchParams, pageable);
        assertTrue(result.isEmpty());
        verify(specificationBuilder, times(1)).build(searchParams);
        verify(bookRepository, times(1)).findAll(bookSpec, pageable);
        verify(bookMapper, times(1)).toDto(bookPage);
        verifyNoMoreInteractions(bookRepository, specificationBuilder, bookMapper);
    }

    @Test
    @DisplayName("Find a book by category id")
    void findAllByCategoryId_existsCategory_shouldReturnCorrectBook() {
        Long id = 1L;
        Book book = createBook();
        Pageable pageable = createPageable();
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds(
                book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(), book.getPrice(),
                book.getDescription(), book.getCoverImage()
        );
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAllByCategoryId(id, pageable)).thenReturn(bookPage);
        when(bookMapper.toDtoWithoutCategoryIds(bookPage))
                .thenReturn(List.of(bookDtoWithoutCategoryIds));

        List<BookDtoWithoutCategoryIds> byCategoryId = bookService
                .findAllByCategoryId(id, pageable);
        assertEquals(1, byCategoryId.size());
        assertEquals(bookDtoWithoutCategoryIds, byCategoryId.get(0));
        verify(bookRepository, times(1)).findAllByCategoryId(id, pageable);
        verify(bookMapper, times(1)).toDtoWithoutCategoryIds(bookPage);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    private CreateBookRequestDto createBookRequestDto() {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setAuthor("George Orwell");
        bookRequestDto.setTitle("Animal Farm: A Fairy Story");
        bookRequestDto.setIsbn("123456789");
        bookRequestDto.setPrice(new BigDecimal(10));
        bookRequestDto.setCategoryIds(List.of(1L));
        return bookRequestDto;
    }

    private Book createBook() {
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("George Orwell");
        book.setTitle("Animal Farm: A Fairy Story");
        book.setIsbn("123456789");
        book.setPrice(new BigDecimal(10));
        book.setCategories(new HashSet<>(List.of(new Category(1L))));
        return book;
    }

    private BookDto createBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());
        bookDto.setCategoryIds(book.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList()));
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setDescription(book.getDescription());
        return bookDto;
    }

    private Pageable createPageable() {
        return PageRequest.of(0, 10);
    }
}
