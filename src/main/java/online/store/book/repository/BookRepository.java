package online.store.book.repository;

import java.util.List;
import java.util.Optional;
import online.store.book.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> getAll();

    Optional<Book> findBookById(Long id);
}
