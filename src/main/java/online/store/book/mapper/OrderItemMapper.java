package online.store.book.mapper;

import java.util.List;
import online.store.book.config.MapperConfig;
import online.store.book.dto.order.OrderItemResponseDto;
import online.store.book.model.CartItem;
import online.store.book.model.Order;
import online.store.book.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);

    List<OrderItemResponseDto> toDto(Page<OrderItem> orderItems);

    @Mappings({
            @Mapping(target = "order", source = "order"),
            @Mapping(target = "price", source = "cartItem.book.price")
    })
    OrderItem toOrderItem(CartItem cartItem, Order order);
}
