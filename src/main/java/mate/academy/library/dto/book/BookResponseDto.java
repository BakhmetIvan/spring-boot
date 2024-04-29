package mate.academy.library.dto.book;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
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
