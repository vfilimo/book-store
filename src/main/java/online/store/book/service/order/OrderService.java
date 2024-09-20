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

    List<OrderResponseDto> getOrders(User user, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long orderId, OrderUpdateRequestDto orderUpdateRequestDto);

    List<OrderItemResponseDto> getOrderById(User user, Long orderId, Pageable pageable);

    OrderItemResponseDto getOrderItemById(User user, Long orderId, Long itemId);
}
