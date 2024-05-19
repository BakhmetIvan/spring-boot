package mate.academy.library.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderUpdateStatusDto {
    @NotBlank
    @Size(max = 50)
    private String status;
}
