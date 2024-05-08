package mate.academy.library.service;

import mate.academy.library.dto.shoppingcart.CartItemDto;
import mate.academy.library.dto.shoppingcart.CartItemRequestDto;
import mate.academy.library.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.library.model.ShoppingCart;
import mate.academy.library.model.User;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    ShoppingCart save(User user);

    CartItemDto saveCartItem(CartItemRequestDto requestDto, User user);

    ShoppingCartResponseDto getShoppingCart(User user, Pageable pageable);

    CartItemDto updateQuantity(Long id, int quantity, User user);

    void deleteCartItemById(Long id, User user);
}
