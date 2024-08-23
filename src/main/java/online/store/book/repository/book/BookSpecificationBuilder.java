package online.store.book.repository.book;

import lombok.RequiredArgsConstructor;
import online.store.book.dto.BookSearchParameters;
import online.store.book.model.Book;
import online.store.book.repository.SpecificationBuilder;
import online.store.book.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (bookSearchParameters.authors() != null && bookSearchParameters.authors().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider("authors")
                    .getSpecification(bookSearchParameters.authors()));
        }
        if (bookSearchParameters.titles() != null && bookSearchParameters.titles().length > 0) {
            specification = specification.and(
                    specificationProviderManager.getSpecificationProvider("titles")
                    .getSpecification(bookSearchParameters.titles()));
        }
        return specification;
    }
}
