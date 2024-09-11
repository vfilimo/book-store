package online.store.book.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.store.book.exceptions.SpecificationProviderException;
import online.store.book.model.Book;
import online.store.book.repository.specification.SpecificationProvider;
import online.store.book.repository.specification.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> specificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationProviderException(
                        "Can't find correct specification provider for key: " + key));
    }
}
