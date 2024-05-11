package mate.academy.library.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.shoppingcart.CartItemDto;
import mate.academy.library.dto.shoppingcart.CartItemRequestDto;
import mate.academy.library.dto.shoppingcart.CartItemUpdateRequestDto;
import mate.academy.library.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.mapper.CartItemMapper;
import mate.academy.library.mapper.ShoppingCartMapper;
import mate.academy.library.model.Book;
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
import java.util.LinkedHashSet;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public CartItemDto saveCartItem(CartItemRequestDto requestDto, User user) {
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: "
                        + requestDto.getBookId())
        );
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(user);
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(User user, Pageable pageable) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(user);
        Page<CartItem> cartItems = cartItemRepository.
                findAllByShoppingCartId(shoppingCart.getId(), pageable);
        shoppingCart.setCartItems(new LinkedHashSet<>(cartItems.getContent()));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public CartItemDto updateQuantity(Long id, CartItemUpdateRequestDto requestDto, User user) {
        ShoppingCart cart = shoppingCartRepository.findShoppingCartByUser(user);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, cart.getId())
                .map(item -> {
                    item.setQuantity(requestDto.getQuantity());
                    return item;
                }).orElseThrow(() -> new EntityNotFoundException(
                        String.format("No cart item with id: %d for user: %d",
                                id, user.getId())
                ));
        cartItemRepository.save(cartItem);
        return cartItemMapper.toDto(cartItem);
    }

    @Override
    public void deleteCartItemById(Long id, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(user);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                String.format("No cart item with id: %d for user: %d",
                                        id, user.getId())
                        ));
        cartItemRepository.delete(cartItem);
    }
}
