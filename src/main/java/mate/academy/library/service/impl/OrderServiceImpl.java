package mate.academy.library.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.order.OrderItemResponseDto;
import mate.academy.library.dto.order.OrderRequestDto;
import mate.academy.library.dto.order.OrderResponseDto;
import mate.academy.library.dto.order.OrderUpdateStatusDto;
import mate.academy.library.exception.EntityNotFoundException;
import mate.academy.library.mapper.OrderItemMapper;
import mate.academy.library.mapper.OrderMapper;
import mate.academy.library.model.CartItem;
import mate.academy.library.model.Order;
import mate.academy.library.model.OrderItem;
import mate.academy.library.model.ShoppingCart;
import mate.academy.library.model.User;
import mate.academy.library.repository.order.OrderItemRepository;
import mate.academy.library.repository.order.OrderRepository;
import mate.academy.library.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.library.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderResponseDto createOrder(User user, OrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUser(user);
        Order order = orderMapper.toModel(requestDto);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        order.setTotal(countTotal(shoppingCart.getCartItems()));
        orderRepository.save(order);
        order.setOrderItems(setOrderItemsFromShoppingCart(order, shoppingCart.getCartItems()));
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDto> findAllOrders(User user, Pageable pageable) {
        return orderRepository.findAllByUser(user, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public OrderResponseDto updateOrderStatus(Long id, OrderUpdateStatusDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Can't find order by %d id", id))
        );
        orderMapper.updateOrderFromDto(order, requestDto);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Transactional
    @Override
    public List<OrderItemResponseDto> findAllOrderItems(Long id, Pageable pageable, User user) {
        if (!isUserValid(user.getId(), id)) {
            throw new RuntimeException("User don't have permission to access the order");
        }
        return orderItemRepository.findAllByOrderId(id, pageable).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto findOrderItemByOrderId(Long orderId, Long itemId, User user) {
        if (!isUserValid(user.getId(), orderId)) {
            throw new RuntimeException("User don't have permission to access the order");
        }
        return orderItemMapper.toDto(orderItemRepository.findByIdAndOrderId(itemId, orderId));
    }

    private BigDecimal countTotal(Set<CartItem> cartItems) {
        return BigDecimal.valueOf(cartItems.stream()
                .mapToDouble(pr ->
                        pr.getQuantity() * pr.getBook().getPrice().doubleValue()
                ).sum());
    }

    private Set<OrderItem> setOrderItemsFromShoppingCart(Order order, Set<CartItem> cartItems) {
        return cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            return orderItemRepository.save(orderItem);
        }).collect(Collectors.toSet());
    }

    private boolean isUserValid(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Can't find order by %d id", orderId))
        );
        return order.getUser().getId().equals(userId);
    }
}
