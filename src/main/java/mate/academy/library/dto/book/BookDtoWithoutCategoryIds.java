package mate.academy.library.dto.book;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BookDtoWithoutCategoryIds {
    Long id;
    String title;
    String author;
    String isbn;
    BigDecimal price;
    String description;
    String coverImage;
}
