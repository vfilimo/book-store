package online.store.book.service.user;

import online.store.book.dto.user.UserRegistrationRequestDto;
import online.store.book.dto.user.UserResponseDto;
import online.store.book.exceptions.RegistrationException;

public interface UserService {
    public UserResponseDto register(UserRegistrationRequestDto requestUser)
            throws RegistrationException;
}
