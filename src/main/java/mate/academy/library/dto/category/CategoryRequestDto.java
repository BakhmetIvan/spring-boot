package mate.academy.library.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank
    @Size(max = 255)
    private String name;
    @Size(max = 255)
    private String description;
}
