package online.store.book.mapper;

import online.store.book.config.MapperConfig;
import online.store.book.dto.user.UserRegistrationRequestDto;
import online.store.book.dto.user.UserResponseDto;
import online.store.book.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toResponseUser(User user);

    User toUserEntity(UserRegistrationRequestDto userRegistrationRequestDto);
}
