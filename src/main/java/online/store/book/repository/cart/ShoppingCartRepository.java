package online.store.book.repository.cart;

import java.util.Optional;
import online.store.book.model.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    @Query("SELECT sc FROM ShoppingCart sc JOIN sc.user u WHERE u.id = :id")
    Optional<ShoppingCart> findByUserId(@Param("id") Long id);
}
