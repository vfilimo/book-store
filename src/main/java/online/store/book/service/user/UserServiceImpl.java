package online.store.book.service.user;

import lombok.RequiredArgsConstructor;
import online.store.book.dto.user.UserRegistrationRequestDto;
import online.store.book.dto.user.UserResponseDto;
import online.store.book.exceptions.RegistrationException;
import online.store.book.mapper.UserMapper;
import online.store.book.model.User;
import online.store.book.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestUser)
            throws RegistrationException {
        if (userRepository.findUserByEmail(requestUser.getEmail()).isPresent()) {
            throw new RegistrationException("User with email: "
                    + requestUser.getEmail() + " exists");
        }
        User user = new User();
        user.setEmail(requestUser.getEmail());
        user.setPassword(requestUser.getPassword());
        user.setFirstName(requestUser.getFirstName());
        user.setLastName(requestUser.getLastName());
        user.setShippingAddress(requestUser.getShippingAddress());
        return userMapper.toResponseUser(userRepository.save(user));
    }
}
