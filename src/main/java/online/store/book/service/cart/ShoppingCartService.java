package online.store.book.service.cart;

import online.store.book.dto.cart.CartItemRequestDto;
import online.store.book.dto.cart.CartItemResponseDto;
import online.store.book.dto.cart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart(String email);

    CartItemResponseDto updateShoppingCart(String email, Long cartItemId, int quantity);

    CartItemResponseDto addBook(String email, CartItemRequestDto cartItemRequestDto);

    void delete(String email, Long cartItemId);
}
