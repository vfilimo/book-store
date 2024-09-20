package online.store.book.service.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import online.store.book.dto.order.OrderItemResponseDto;
import online.store.book.dto.order.OrderRequestDto;
import online.store.book.dto.order.OrderResponseDto;
import online.store.book.dto.order.OrderUpdateRequestDto;
import online.store.book.exceptions.EntityNotFoundException;
import online.store.book.mapper.OrderItemMapper;
import online.store.book.mapper.OrderMapper;
import online.store.book.model.Order;
import online.store.book.model.OrderItem;
import online.store.book.model.ShoppingCart;
import online.store.book.model.User;
import online.store.book.repository.cart.ShoppingCartRepository;
import online.store.book.repository.order.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto saveOrder(User user, OrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = getShoppingCartByUser(user);
        Order order = createOrder(user, orderRequestDto, shoppingCart);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        return orderMapper.toDto(orderRepository.save(order));
    }

    private ShoppingCart getShoppingCartByUser(User user) {
        return shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User: "
                        + user.getEmail() + " doesn't have shopping cart"));
    }

    private Order createOrder(User user, OrderRequestDto orderRequestDto,
                              ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequestDto.shippingAddress());
        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(item -> orderItemMapper.toOrderItem(item, order))
                .collect(Collectors.toSet());
        order.setOrderItems(orderItems);
        BigDecimal totalPrice = order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotal(totalPrice);
        return order;
    }

    @Override
    public List<OrderResponseDto> getOrders(User user, Pageable pageable) {
        return orderMapper.toDto(orderRepository.findByUserId(user.getId(), pageable));
    }

    @Override
    public OrderResponseDto updateOrderStatus(
            Long orderId, OrderUpdateRequestDto orderUpdateRequestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order with id: " + orderId));
        order.setStatus(orderUpdateRequestDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderItemResponseDto> getOrderById(User user, Long orderId, Pageable pageable) {
        return orderItemMapper.toDto(
                orderRepository.findItemsById(user.getId(), orderId, pageable));
    }

    @Override
    public OrderItemResponseDto getOrderItemById(User user, Long orderId, Long itemId) {
        OrderItem orderItem = orderRepository.findItemById(user.getId(), orderId, itemId)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format(
                                "You don't have order item with id: %d in order with id: %d",
                                itemId, orderId))
                );
        return orderItemMapper.toDto(orderItem);
    }
}
