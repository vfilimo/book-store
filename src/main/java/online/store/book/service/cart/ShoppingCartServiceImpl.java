package online.store.book.service.cart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import online.store.book.dto.cart.CartItemRequestDto;
import online.store.book.dto.cart.ShoppingCartResponseDto;
import online.store.book.exceptions.EntityNotFoundException;
import online.store.book.mapper.ShoppingCartMapper;
import online.store.book.model.Book;
import online.store.book.model.CartItem;
import online.store.book.model.ShoppingCart;
import online.store.book.repository.book.BookRepository;
import online.store.book.repository.cart.ShoppingCartRepository;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;

    @ReadOnlyProperty
    @Override
    public ShoppingCartResponseDto getShoppingCart(String email) {
        ShoppingCart shoppingCart = shoppingCartRepository.findAllFieldsByUserEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException("User: "
                                + email + " doesn't have shopping cart")
                );
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto addBook(String email, CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findAllFieldsByUserEmail(email)
                .orElseThrow(
                    () -> new EntityNotFoundException("You don't have shopping cart")
        );
        shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(cartItemRequestDto.getBookId()))
                .findFirst()
                .ifPresentOrElse(item -> item.setQuantity(item.getQuantity()
                                + cartItemRequestDto.getQuantity()),
                        () -> addCartItemToCart(cartItemRequestDto, shoppingCart));
        ShoppingCart savedShoppingCart = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(savedShoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto updateShoppingCart(String email, Long cartItemId, int quantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findAllFieldsByUserEmail(email)
                .orElseThrow(
                    () -> new EntityNotFoundException("User: "
                        + email + " doesn't have shopping cart"));
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("You don't have cart item with id: "
                        + cartItemId));
        cartItem.setQuantity(quantity);
        ShoppingCart updatedCart = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(updatedCart);
    }

    @Transactional
    @Override
    public void delete(String email, Long cartItemId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findAllFieldsByUserEmail(email)
                .orElseThrow(
                    () -> new EntityNotFoundException("User: "
                        + email + " doesn't have shopping cart"
        ));
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("You don't have cart item with id: "
                        + cartItemId));
        shoppingCart.getCartItems().remove(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    private void addCartItemToCart(CartItemRequestDto cartItemRequestDto,
                                           ShoppingCart shoppingCart) {
        Book book = bookRepository.findById(cartItemRequestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id: "
                        + cartItemRequestDto.getBookId())
        );
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
    }
}
