package mate.academy.library.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.order.OrderItemResponseDto;
import mate.academy.library.dto.order.OrderRequestDto;
import mate.academy.library.dto.order.OrderResponseDto;
import mate.academy.library.dto.order.OrderUpdateStatusDto;
import mate.academy.library.model.User;
import mate.academy.library.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Orders management", description = "Endpoint for mapping orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create new order",
            description = "Creates a new order for the user that will contain " +
                    "items from shopping cart. After order was created, " +
                    "the shopping cart is cleared")
    public OrderResponseDto createOrder(Authentication authentication,
                                        @RequestBody @Valid OrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(user, requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Returns all user's orders",
            description = "Returns a list of the user's orders using pagination and sorting")
    public List<OrderResponseDto> findAllOrders(Authentication authentication,
                                                @PageableDefault Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrders(user, pageable);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update status of order",
            description = "End point for admin which serves to update the order status")
    public OrderResponseDto updateOrderStatus(@Positive @PathVariable Long id,
                                              @RequestBody @Valid OrderUpdateStatusDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }

    @GetMapping("/{id}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Returns all items which contained in the order",
            description = "Returns a list of the order items using pagination and sorting")
    public List<OrderItemResponseDto> findAllOrderItems(Authentication authentication,
                                                        @Positive @PathVariable Long id,
                                                        @PageableDefault Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrderItems(id, pageable, user);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Returns the order item",
            description = "Returns a single order item using order id and item id to search")
    public OrderItemResponseDto findOrderItemByOrderId(Authentication authentication,
                                                       @Positive @PathVariable Long orderId,
                                                       @Positive @PathVariable Long itemId) {
        User user = (User) authentication.getPrincipal();
        return orderService.findOrderItemByOrderId(orderId, itemId, user);
    }
}
