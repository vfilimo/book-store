package online.store.book.service.cart;

import online.store.book.dto.cart.CartItemRequestDto;
import online.store.book.dto.cart.ShoppingCartResponseDto;
import online.store.book.dto.cart.UpdateCartItemRequestDto;
import online.store.book.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart(User user);

    ShoppingCartResponseDto updateShoppingCart(User user, Long cartItemId,
                                               UpdateCartItemRequestDto updateCartItem);

    ShoppingCartResponseDto addBook(User user, CartItemRequestDto cartItemRequestDto);

    void delete(User user, Long cartItemId);
}
