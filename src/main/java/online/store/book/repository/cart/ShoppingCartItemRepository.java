package online.store.book.repository.cart;

import java.util.Optional;
import online.store.book.model.CartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = "book")
    @Query("SELECT ci FROM CartItem ci "
            + "JOIN ci.shoppingCart sc "
            + "WHERE sc.id = :shoppingCartId AND ci.id = :itemId")
    Optional<CartItem> findByShoppingCartIdAndItemId(@Param("shoppingCartId") Long shoppingCartId,
                                                     @Param("itemId") Long itemId);

    @EntityGraph(attributePaths = {"book"})
    @Query("SELECT ci FROM CartItem ci WHERE ci.id = :itemId")
    Optional<CartItem> findByItemId(@Param("itemId") Long itemId);
}
