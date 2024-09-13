package online.store.book.service.book;

import java.util.List;
import online.store.book.dto.book.BookDto;
import online.store.book.dto.book.BookDtoWithoutCategoryIds;
import online.store.book.dto.book.BookSearchParameters;
import online.store.book.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findBookById(Long id);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    void delete(Long id);

    List<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id, Pageable pageable);
}
