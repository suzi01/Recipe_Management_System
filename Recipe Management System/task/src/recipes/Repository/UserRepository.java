package recipes.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.Entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    boolean existsUserByEmail(String email);

    Optional<User> findUserByPassword(String password);
}
