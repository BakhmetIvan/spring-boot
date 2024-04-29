package mate.academy.library.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class CreateBookRequestDto {
        @NotBlank
        String title;
        @NotBlank
        String author;
        @NotBlank
        String isbn;
        @NotNull
        @Min(0)
        BigDecimal price;
        Set<Long> categoryIds;
        String description;
        String coverImage;
}
