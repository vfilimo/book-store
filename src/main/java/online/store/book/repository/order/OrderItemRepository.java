package online.store.book.repository.order;

import java.util.Optional;
import online.store.book.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi "
            + "JOIN FETCH oi.order o "
            + "WHERE o.user.id = :userId AND o.id = :orderId AND oi.id = :itemId")
    Optional<OrderItem> findByIdAndOrderIdAndUserId(
            @Param("userId") Long userId, @Param("orderId") Long orderId,
            @Param("itemId") Long itemId);

}
