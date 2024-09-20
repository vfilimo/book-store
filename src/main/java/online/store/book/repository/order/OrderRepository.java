package online.store.book.repository.order;

import java.util.Optional;
import online.store.book.model.Order;
import online.store.book.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN o.user u WHERE u.id = :userId")
    @EntityGraph(attributePaths = {"user", "orderItems", "orderItems.book"})
    Page<Order> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT oi "
            + "FROM Order o JOIN o.orderItems oi JOIN o.user u "
            + "WHERE u.id = :userId AND o.id = :orderId")
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    Page<OrderItem> findItemsById(@Param("userId") Long userId,
                                 @Param("orderId") Long orderId, Pageable pageable);

    @Query("SELECT oi FROM Order o JOIN o.user u JOIN o.orderItems oi "
            + "WHERE u.id = :userId AND o.id = :orderId AND oi.id = :itemId")
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    Optional<OrderItem> findItemById(
            @Param("userId") Long userId, @Param("orderId") Long orderId,
            @Param("itemId") Long itemId);
}
