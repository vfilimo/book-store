package online.store.book.mapper;

import online.store.book.config.MapperConfig;
import online.store.book.dto.cart.ShoppingCartResponseDto;
import online.store.book.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = ShoppingCartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);
}
