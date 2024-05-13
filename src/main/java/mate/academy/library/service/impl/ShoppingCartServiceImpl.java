package mate.academy.library.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.shoppingcart.CartItemRequestDto;
import mate.academy.library.dto.shoppingcart.CartItemUpdateRequestDto;
import mate.academy.library.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.mapper.ShoppingCartMapper;
import mate.academy.library.model.Book;
import mate.academy.library.model.CartItem;
import mate.academy.library.model.ShoppingCart;
import mate.academy.library.model.User;
import mate.academy.library.repository.book.BookRepository;
import mate.academy.library.repository.cartitem.CartItemRepository;
import mate.academy.library.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.library.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto saveCartItem(CartItemRequestDto requestDto, User user) {
        Book book = bookRepository.findById(requestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by id: "
                        + requestDto.getBookId())
        );
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(user);
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCart(User user) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findShoppingCartByUser(user));
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto updateQuantity(Long id, CartItemUpdateRequestDto requestDto, User user) {
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
        return shoppingCartMapper.toDto(cart);
    }

    @Transactional
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
