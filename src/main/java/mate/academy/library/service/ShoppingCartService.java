package mate.academy.library.service;

import mate.academy.library.dto.shoppingcart.CartItemRequestDto;
import mate.academy.library.dto.shoppingcart.CartItemUpdateRequestDto;
import mate.academy.library.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.library.model.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCartResponseDto saveCartItem(CartItemRequestDto requestDto, User user);

    ShoppingCartResponseDto getShoppingCart(User user);

    ShoppingCartResponseDto updateQuantity(Long id, CartItemUpdateRequestDto requestDto, User user);

    void deleteCartItemById(Long id, User user);
}
