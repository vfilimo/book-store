package online.store.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
        String actualAuthor = actualFirstCategory.stream()
                .findFirst()
                .get()
                .getAuthor();
        assertEquals(1L, actualFirstCategory.getTotalElements());
        assertEquals("John Doe", actualAuthor);

        Page<Book> actualSecondCategory = bookRepository.findAllByCategoryId(2L, pageable);
        String actualTitle = actualSecondCategory.stream()
                .findFirst()
                .get()
                .getTitle();
        assertEquals(2L, actualSecondCategory.getTotalElements());
        assertEquals("Horror Stories", actualTitle);
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
        boolean isPresent = bookPage.stream()
                .findFirst()
                .isPresent();
        assertFalse(isPresent);
    }
}
