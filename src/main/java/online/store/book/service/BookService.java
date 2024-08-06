package online.store.book.service;

import java.util.List;
import online.store.book.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> getAll();
}
