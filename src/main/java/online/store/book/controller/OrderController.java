package online.store.book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 5;
    private static final String DEFAULT_SORT_PARAMETER = "id";
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create new order by you shopping cart",
            description = "After placing an order, your shopping cart will be cleared")
    @PostMapping
    public OrderResponseDto placeOrder(@RequestBody @Valid OrderRequestDto orderRequestDto) {
        User user = getUserFromContext();
        return orderService.saveOrder(user, orderRequestDto);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get order history")
    @GetMapping
    public List<OrderResponseDto> getAllOrders(@PageableDefault(size = DEFAULT_PAGE_SIZE,
            page = DEFAULT_PAGE, sort = DEFAULT_SORT_PARAMETER) Pageable pageable) {
        User user = getUserFromContext();
        return orderService.getOrders(user.getId(), pageable);
    }

    @Operation(summary = "Update order status", description = "Only for Admin role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{orderId}")
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody @Valid OrderUpdateRequestDto orderUpdateRequestDto) {
        return orderService.updateOrderStatus(orderId, orderUpdateRequestDto);
    }

    @Operation(summary = "Find specific order by order id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDto> getSpecificOrder(@PathVariable Long orderId) {
        User user = getUserFromContext();
        return orderService.getOrderById(user.getId(), orderId);
    }

    @Operation(summary = "Find specific order item by id and order id")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDto getSpecificOrderItem(
            @PathVariable Long orderId, @PathVariable Long itemId) {
        User user = getUserFromContext();
        return orderService.getOrderItemById(user.getId(), orderId, itemId);
    }

    private User getUserFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
