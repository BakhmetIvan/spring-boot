package mate.academy.library.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.library.model.Book;
import mate.academy.library.repository.SpecificationProvider;
import mate.academy.library.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviderList;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviderList.stream()
                .filter(specProvider -> specProvider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Can't find specifications or key " + key));
    }
}
