package online.store.book;

import java.math.BigDecimal;
import online.store.book.model.Book;
import online.store.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = new Book();
                book.setAuthor("Ivan Buy");
                book.setTitle("Dnipro");
                book.setPrice(BigDecimal.valueOf(100));
                book.setIsbn("123456789");
                bookService.save(book);
                System.out.println(bookService.getAll());
            }
        };
    }
}
