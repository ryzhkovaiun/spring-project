package services;

import dto.UserDto;
import entities.Role;
import entities.User;
import repositories.RoleRepository;
import repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final String USER_ROLE_NAME = "USER";

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(UserDto userDto) {
        User user = new User();

        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(List.of(roleRepository.
            findByName(USER_ROLE_NAME).
            orElseGet(this::checkRoleExist)
        ));

        userRepository.save(user);
    }

    private Role checkRoleExist() {
        Role role = new Role();

        role.setName(USER_ROLE_NAME);

        return roleRepository.save(role);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}