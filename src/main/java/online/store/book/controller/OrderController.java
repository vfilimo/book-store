package online.store.book.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.store.book.dto.order.OrderItemResponseDto;
import online.store.book.dto.order.OrderRequestDto;
import online.store.book.dto.order.OrderResponseDto;
import online.store.book.dto.order.OrderUpdateRequestDto;
import online.store.book.model.User;
import online.store.book.service.order.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    OrderResponseDto placeOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        User user = getUserFromContext();
        return orderService.saveOrder(user, orderRequestDto);
    }

    @GetMapping
    List<OrderResponseDto> getAllOrders(Pageable pageable) {
        User user = getUserFromContext();
        return orderService.getOrders(user, pageable);
    }

    @PatchMapping("/{id}")
    OrderResponseDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody @Valid OrderUpdateRequestDto orderUpdateRequestDto) {
        User user = getUserFromContext();
        return orderService.updateOrderStatus(user, orderUpdateRequestDto);
    }

    @GetMapping("/{orderId}/items")
    List<OrderItemResponseDto> getSpecificOrder(@PathVariable Long orderId, Pageable pageable) {
        User user = getUserFromContext();
        return orderService.getOrderById(user, orderId, pageable);
    }

    @GetMapping("{orderId}/items/{itemId}")
    OrderItemResponseDto getSpecificOrderItem(
            @PathVariable Long orderId, @PathVariable Long itemId) {
        User user = getUserFromContext();
        return orderService.getOrderItemById(user, orderId, itemId);
    }

    private User getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
