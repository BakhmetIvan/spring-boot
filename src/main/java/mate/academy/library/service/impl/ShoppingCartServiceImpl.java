package mate.academy.library.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.shoppingcart.CartItemDto;
import mate.academy.library.dto.shoppingcart.CartItemRequestDto;
import mate.academy.library.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.mapper.CartItemMapper;
import mate.academy.library.mapper.ShoppingCartMapper;
import mate.academy.library.model.CartItem;
import mate.academy.library.model.ShoppingCart;
import mate.academy.library.model.User;
import mate.academy.library.repository.book.BookRepository;
import mate.academy.library.repository.cartitem.CartItemRepository;
import mate.academy.library.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.library.service.ShoppingCartService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public ShoppingCart save(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public CartItemDto saveCartItem(CartItemRequestDto requestDto, User user) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(shoppingCartRepository.findShoppingCartByUser(user));
        cartItem.setBook(bookRepository.findById(requestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: "
                        + requestDto.getBookId())
        ));
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(User user, Pageable pageable) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(user);
        Page<CartItem> cartItems = cartItemRepository.
                findAllByShoppingCartId(shoppingCart.getId(), pageable);
        shoppingCart.setCartItems(cartItems.toSet());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public CartItemDto updateQuantity(Long id, int quantity, User user) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("can't find cart item by id: " + id)
        );
        if (!Objects.equals(cartItem.getShoppingCart().getUser().getId(), user.getId())) {
            throw new RuntimeException("The user doesn't have access to this cart item");
        }
        cartItem.setQuantity(quantity);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteCartItemById(Long id, User user) {
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("can't find cart item by id: " + id)
        );
        if (!Objects.equals(cartItem.getShoppingCart().getUser().getId(), user.getId())) {
            throw new RuntimeException("The user doesn't have access to this cart item");
        }
        cartItemRepository.delete(cartItem);
    }
}
