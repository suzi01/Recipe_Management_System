package recipes.Service.Impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import recipes.Exceptions.UserAlreadyExistsException;
import recipes.Models.UserDto;
import recipes.Service.UserService;
import recipes.UseAdapter.UserAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.Entity.User;
import recipes.Repository.UserRepository;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository
                .findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new UserAdapter(user);
    }



    @Override
    public void verifyUser(UserDto userDto) {
        if (repository.existsUserByEmail(userDto.getEmail())) {
            throw new UserAlreadyExistsException();
        }

        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setAuthority("USER");
        repository.save(newUser);
    }
}
