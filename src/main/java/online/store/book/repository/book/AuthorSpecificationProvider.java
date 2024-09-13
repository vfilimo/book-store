package online.store.book.repository.book;

import java.util.Arrays;
import online.store.book.model.Book;
import online.store.book.repository.specification.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String FIELD_NAME = "author";

    @Override
    public String getKey() {
        return FIELD_NAME;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(FIELD_NAME)
                .in(Arrays.stream(params).toArray());
    }
}
