package online.store.book.service;

import online.store.book.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> getAll();
}
