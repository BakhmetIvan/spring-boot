package mate.academy.library.service;

import mate.academy.library.dto.shoppingcart.CartItemDto;
import mate.academy.library.dto.shoppingcart.CartItemRequestDto;
import mate.academy.library.dto.shoppingcart.CartItemUpdateRequestDto;
import mate.academy.library.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.library.model.User;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    CartItemDto saveCartItem(CartItemRequestDto requestDto, User user);

    ShoppingCartResponseDto getShoppingCart(User user, Pageable pageable);

    CartItemDto updateQuantity(Long id, CartItemUpdateRequestDto requestDto, User user);

    void deleteCartItemById(Long id, User user);
}
