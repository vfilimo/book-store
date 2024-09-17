package online.store.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.store.book.dto.cart.CartItemRequestDto;
import online.store.book.dto.cart.ShoppingCartResponseDto;
import online.store.book.model.User;
import online.store.book.service.cart.ShoppingCartService;
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
        String email = getEmailFromContext();
        return shoppingCartService.addBook(email, cartItemRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Shows all items in you shopping cart")
    @GetMapping
    public ShoppingCartResponseDto get() {
        String email = getEmailFromContext();
        return shoppingCartService.getShoppingCart(email);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Update you cart item",
            description = "You can update the quantity of your books")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartResponseDto updateCartItem(
            @PathVariable Long cartItemId, @RequestBody int quantity) {
        String email = getEmailFromContext();
        return shoppingCartService.updateShoppingCart(email, cartItemId, quantity);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Delete cart item from you shopping cart",
            description = "You can delete book from you shopping cart")
    @DeleteMapping("/items/{cartItemId}")
    public void deleteCartItem(@PathVariable Long cartItemId) {
        String email = getEmailFromContext();
        shoppingCartService.delete(email, cartItemId);
    }

    private String getEmailFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getEmail();
    }
}
