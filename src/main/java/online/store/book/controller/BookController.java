package online.store.book.controller;

import lombok.RequiredArgsConstructor;
import online.store.book.dto.BookDto;
import online.store.book.dto.CreateBookRequestDto;
import online.store.book.service.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @PostMapping
    public BookDto createBook(@RequestBody CreateBookRequestDto bookDto) {
        return bookService.save(bookDto);
    }
}
