package online.store.book.repository.user;

import java.util.Optional;
import online.store.book.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    boolean existsByEmail(String email);
}
