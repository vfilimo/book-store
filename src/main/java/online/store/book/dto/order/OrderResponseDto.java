package online.store.book.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import online.store.book.model.Order;

public record OrderResponseDto(
        Long id,
        Long userId,
        List<OrderItemResponseDto> orderItems,
        LocalDateTime orderDate,
        BigDecimal total,
        Order.Status status
) {
}
