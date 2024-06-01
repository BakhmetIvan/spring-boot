package mate.academy.library.dto.book;

import lombok.Data;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.util.Set;

@Data
@Accessors(chain = true)
public class BookResponseDto {
        Long id;
        String title;
        String author;
        String isbn;
        BigDecimal price;
        String description;
        String coverImage;
        Set<Long> categoryIds;
}
