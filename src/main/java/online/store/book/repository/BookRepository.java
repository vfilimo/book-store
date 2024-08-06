package online.store.book.repository;

import online.store.book.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> getAll();
}
