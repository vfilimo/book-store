package online.store.book.mapper;

import java.util.List;
import java.util.Set;
import online.store.book.config.MapperConfig;
import online.store.book.dto.order.OrderItemResponseDto;
import online.store.book.model.CartItem;
import online.store.book.model.Order;
import online.store.book.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "bookId", source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);

    List<OrderItemResponseDto> toDto(Set<OrderItem> orderItems);

    @Mappings({
            @Mapping(target = "order", source = "order"),
            @Mapping(target = "price", source = "cartItem.book.price"),
            @Mapping(target = "id", ignore = true)
    })
    OrderItem toOrderItem(CartItem cartItem, Order order);
}
