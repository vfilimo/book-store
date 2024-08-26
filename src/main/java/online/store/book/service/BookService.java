package online.store.book.service;

import java.util.List;
import online.store.book.dto.BookDto;
import online.store.book.dto.BookSearchParameters;
import online.store.book.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll();

    BookDto findBookById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    void delete(Long id);

    List<BookDto> search(BookSearchParameters bookSearchParameters);
}
