package online.store.book.service.cart;

import jakarta.persistence.FetchType;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import online.store.book.dto.cart.CartItemRequestDto;
import online.store.book.dto.cart.CartItemResponseDto;
import online.store.book.dto.cart.ShoppingCartResponseDto;
import online.store.book.exceptions.EntityNotFoundException;
import online.store.book.mapper.ShoppingCartItemMapper;
import online.store.book.mapper.ShoppingCartMapper;
import online.store.book.model.CartItem;
import online.store.book.model.ShoppingCart;
import online.store.book.repository.cart.ShoppingCartItemRepository;
import online.store.book.repository.cart.ShoppingCartRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartItemMapper shoppingCartItemMapper;

    @Override
    public ShoppingCartResponseDto getShoppingCart(String email) {
        ShoppingCart shoppingCart = shoppingCartRepository.findAllFieldsByUserEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User: " + email + " doesn't have shopping cart")
        );
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public CartItemResponseDto addBook(String email, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User: " + email + " doesn't have shopping cart")
        );
        CartItem cartItem = shoppingCartItemMapper.toEntity(cartItemRequestDto);
        cartItem.setShoppingCart(shoppingCart);
        CartItem savedCartItem = shoppingCartItemRepository.save(cartItem);
        Long id = savedCartItem.getId();
        CartItem item = shoppingCartItemRepository.findByShoppingCartIdAndItemId(shoppingCart.getId(), id).orElseThrow();
        return shoppingCartItemMapper.toDto(item);
    }

    @Transactional
    @Override
    public CartItemResponseDto updateShoppingCart(String email, Long cartItemId, int quantity) {
        Long shoppingCartId = shoppingCartRepository.findByUserEmail(email).orElseThrow(
                () -> new EntityNotFoundException("User: "
                        + email + " doesn't have shopping cart")).getId();
        CartItem cartItem = shoppingCartItemRepository
                .findByShoppingCartIdAndItemId(shoppingCartId, cartItemId).orElseThrow(
                    () -> new EntityNotFoundException("You don't have cart item with id: "
                        + cartItemId));
        cartItem.setQuantity(quantity);
        CartItem savedCartItem = shoppingCartItemRepository.save(cartItem);
        return shoppingCartItemMapper.toDto(savedCartItem);
    }

    @Override
    public void delete(String email, Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email).orElseThrow();
        Long shoppingCartId = shoppingCart.getId();
        CartItem cartItem = shoppingCartItemRepository
                .findByShoppingCartIdAndItemId(shoppingCartId, cartItemId).orElseThrow(
                        () -> new EntityNotFoundException("You don't have cart item with id: "
                                + cartItemId));
        shoppingCartItemRepository.delete(cartItem);
    }
}
