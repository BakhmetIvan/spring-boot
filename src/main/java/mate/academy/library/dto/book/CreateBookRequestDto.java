package mate.academy.library.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateBookRequestDto {
        @NotNull
        String title;
        @NotNull
        String author;
        @NotNull
        String isbn;
        @NotNull
        @Min(0)
        BigDecimal price;
        String description;
        String coverImage;
}
