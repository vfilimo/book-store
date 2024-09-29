package online.store.book.service.order;

import java.util.List;
import online.store.book.dto.order.OrderItemResponseDto;
import online.store.book.dto.order.OrderRequestDto;
import online.store.book.dto.order.OrderResponseDto;
import online.store.book.dto.order.OrderUpdateRequestDto;
import online.store.book.model.User;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto saveOrder(User user, OrderRequestDto orderRequestDto);

    List<OrderResponseDto> getOrders(Long userId, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long orderId, OrderUpdateRequestDto orderUpdateRequestDto);

    List<OrderItemResponseDto> getOrderById(Long userId, Long orderId);

    OrderItemResponseDto getOrderItemById(Long userId, Long orderId, Long itemId);
}
