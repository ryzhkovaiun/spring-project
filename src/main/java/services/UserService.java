package services;

import dto.UserDto;
import entities.User;

import java.util.Optional;

public interface UserService {

    void save(UserDto userDto);

    Optional<User> findByUsername(String username);

}