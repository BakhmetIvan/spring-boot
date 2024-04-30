package mate.academy.library.dto.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class CreateBookRequestDto {
        @NotBlank
        @Size(max = 255)
        String title;
        @NotBlank
        @Size(max = 255)
        String author;
        @NotBlank
        @Size(max = 255)
        String isbn;
        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal price;
        Set<Long> categoryIds;
        @Size(max = 255)
        String description;
        @Size(max = 255)
        String coverImage;
}
