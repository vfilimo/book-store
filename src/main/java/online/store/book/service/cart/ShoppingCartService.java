package online.store.book.service.cart;

import online.store.book.dto.cart.CartItemRequestDto;
import online.store.book.dto.cart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart(String email);

    ShoppingCartResponseDto updateShoppingCart(String email, Long cartItemId, int quantity);

    ShoppingCartResponseDto addBook(String email, CartItemRequestDto cartItemRequestDto);

    void delete(String email, Long cartItemId);
}
