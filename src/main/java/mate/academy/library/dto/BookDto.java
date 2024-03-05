package mate.academy.library.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookDto {
        Long id;
        String title;
        String author;
        String isbn;
        BigDecimal price;
        String description;
        String coverImage;
}
