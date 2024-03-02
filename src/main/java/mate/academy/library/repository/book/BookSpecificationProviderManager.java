package mate.academy.library.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.library.model.Book;
import mate.academy.library.repository.SpecificationProvider;
import mate.academy.library.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;
import java.util.List;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviderList;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviderList.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find specifications or key " + key));
    }
}
