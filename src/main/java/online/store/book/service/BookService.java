package online.store.book.service;

import java.util.List;
import online.store.book.dto.BookDto;
import online.store.book.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> getAll();

    BookDto findBookById(Long id);
}
