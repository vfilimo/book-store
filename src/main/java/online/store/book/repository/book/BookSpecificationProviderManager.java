package online.store.book.repository.book;

import lombok.RequiredArgsConstructor;
import online.store.book.exceptions.SpecificationProviderException;
import online.store.book.model.Book;
import online.store.book.repository.SpecificationProvider;
import online.store.book.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> specificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationProviderException("Can't find correct specification provider for key: " + key));
    }
}
