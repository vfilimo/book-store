package online.store.book.mapper;

import online.store.book.config.MapperConfig;
import online.store.book.dto.cart.CartItemRequestDto;
import online.store.book.dto.cart.CartItemResponseDto;
import online.store.book.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(config = MapperConfig.class, uses = BookMapper.class)
public interface ShoppingCartItemMapper {
    @Mappings({
            @Mapping(source = "book.id", target = "bookId"),
            @Mapping(source = "book.title", target = "bookTitle")
    })
    CartItemResponseDto toDto(CartItem cartItem);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookFromId")
    CartItem toEntity(CartItemRequestDto requestDto);
}
