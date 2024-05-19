package mate.academy.library.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderRequestDto {
    @NotBlank
    @Size(max = 255)
    private String shoppingAddress;
}
