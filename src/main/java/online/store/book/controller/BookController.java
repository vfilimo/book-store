package online.store.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.store.book.dto.book.BookDto;
import online.store.book.dto.book.BookSearchParameters;
import online.store.book.dto.book.CreateBookRequestDto;
import online.store.book.service.book.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for book management")
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final String DEFAULT_SORT_PARAMETER = "price";
    private final BookService bookService;

    @Operation(summary = "Get all books", description = "Get all books")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BookDto> getAll(
            @PageableDefault(size = DEFAULT_PAGE_SIZE, page = DEFAULT_PAGE,
                    sort = DEFAULT_SORT_PARAMETER) Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all books by search parameters",
            description = "Get all books by title and author")
    @GetMapping("/search")
    public List<BookDto> search(BookSearchParameters bookSearchParameters,
                                @PageableDefault(size = DEFAULT_PAGE_SIZE, page = DEFAULT_PAGE,
                                        sort = DEFAULT_SORT_PARAMETER) Pageable pageable) {
        return bookService.search(bookSearchParameters, pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get book by id", description = "Get book by id")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Save new book", description = "Save new book")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.save(bookRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update book", description = "Update book by id")
    @PutMapping("/{id}")
    public BookDto updateBook(
            @PathVariable Long id,
            @RequestBody @Valid CreateBookRequestDto bookRequestDto) {
        return bookService.update(id, bookRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete book", description = "Delete book by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
