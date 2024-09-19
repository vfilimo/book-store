package online.store.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.store.book.dto.cart.CartItemRequestDto;
import online.store.book.dto.cart.ShoppingCartResponseDto;
import online.store.book.dto.cart.UpdateCartItemRequestDto;
import online.store.book.model.User;
import online.store.book.service.cart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart management")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Add Book to you shopping cart")
    @PostMapping
    public ShoppingCartResponseDto addBook(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        User user = getUserFromContext();
        return shoppingCartService.addBook(user, cartItemRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Shows all items in you shopping cart")
    @GetMapping
    public ShoppingCartResponseDto get() {
        User user = getUserFromContext();
        return shoppingCartService.getShoppingCart(user);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Update you cart item",
            description = "You can update the quantity of your books")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartResponseDto updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemRequestDto updateCartItem) {
        User user = getUserFromContext();
        return shoppingCartService.updateShoppingCart(user, cartItemId, updateCartItem);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Delete cart item from you shopping cart",
            description = "You can delete book from you shopping cart")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable Long cartItemId) {
        User user = getUserFromContext();
        shoppingCartService.delete(user, cartItemId);
    }

    private User getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
