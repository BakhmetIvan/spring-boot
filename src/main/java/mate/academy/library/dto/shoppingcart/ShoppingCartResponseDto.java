package mate.academy.library.dto.shoppingcart;

import lombok.Data;
import java.util.Set;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems;
}
