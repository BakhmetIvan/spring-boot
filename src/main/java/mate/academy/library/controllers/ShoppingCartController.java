package mate.academy.library.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import mate.academy.library.dto.shoppingcart.CartItemDto;
import mate.academy.library.dto.shoppingcart.CartItemRequestDto;
import mate.academy.library.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.library.model.User;
import mate.academy.library.service.ShoppingCartService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
@Tag(name = "Shopping carts management", description = "Endpoint for mapping shopping carts")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get shopping cart",
            description = "Returns the current user's shopping cart")
    public ShoppingCartResponseDto getShoppingCart(Authentication authentication,
                                                   Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCart(user, pageable);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Save cart item",
            description = "Save the cart item for the current user's shopping cart")
    public CartItemDto saveCartItem(@RequestBody @Valid CartItemRequestDto requestDto,
                                    Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.saveCartItem(requestDto, user);
    }

    @PutMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Update book quantity",
            description = "Updates quantity of books for the cart item")
    public CartItemDto updateQuantity(@PathVariable @Positive Long id,
                                      @Positive int quantity,
                                      Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.updateQuantity(id, quantity, user);
    }

    @DeleteMapping("/cart-items/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Delete cart item",
            description = "Deletes cart item from the shopping cart")
    public void deleteCartItemById(@PathVariable @Positive Long id,
                                   Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.deleteCartItemById(id, user);
    }
}
