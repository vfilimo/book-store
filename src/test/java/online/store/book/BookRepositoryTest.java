package online.store.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import online.store.book.model.Book;
import online.store.book.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find a book by category id if the category is exists")
    @Sql(scripts = {
            "classpath:db/categories/add_categories_to_db.sql",
            "classpath:db/books/add_books_with_categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/books/remove_books_from_db.sql",
            "classpath:db/categories/remove_categories_from_db.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_searchByExistingCategory_shouldReturnCorrectNumberOfBooks() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));

        Page<Book> actualFirstCategory = bookRepository.findAllByCategoryId(1L, pageable);

        assertEquals(1L, actualFirstCategory.getTotalElements());

        Page<Book> actualSecondCategory = bookRepository.findAllByCategoryId(2L, pageable);

        assertEquals(2L, actualSecondCategory.getTotalElements());
    }

    @Test
    @DisplayName("Find a book by category id if the category doesn't exists")
    @Sql(scripts = {
            "classpath:db/categories/add_categories_to_db.sql",
            "classpath:db/books/add_books_with_categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/books/remove_books_from_db.sql",
            "classpath:db/categories/remove_categories_from_db.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategoryId_searchByNotExistingCategory_shouldReturnEmpty() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Book> bookPage = bookRepository.findAllByCategoryId(3L, pageable);

        assertTrue(bookPage.getTotalElements() == 0);
    }
}
