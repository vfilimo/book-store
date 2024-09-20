package online.store.book.service.user;

import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import online.store.book.dto.user.UserRegistrationRequestDto;
import online.store.book.dto.user.UserResponseDto;
import online.store.book.exceptions.RegistrationException;
import online.store.book.mapper.UserMapper;
import online.store.book.model.Role;
import online.store.book.model.ShoppingCart;
import online.store.book.model.User;
import online.store.book.repository.cart.ShoppingCartRepository;
import online.store.book.repository.user.RoleRepository;
import online.store.book.repository.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestUser)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestUser.getEmail())) {
            throw new RegistrationException("User with email: "
                    + requestUser.getEmail() + " exists");
        }
        User user = userMapper.toUserEntity(requestUser);
        user.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        Role roleUser = roleRepository.findByName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(roleUser));
        User userFromDb = userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(userFromDb);
        shoppingCartRepository.save(shoppingCart);
        return userMapper.toResponseUser(userFromDb);
    }
}
