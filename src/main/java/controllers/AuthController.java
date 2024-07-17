package controllers;

import jakarta.validation.Valid;
import dto.UserDto;
import entities.User;
import services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserDto());

        return "register";
    }

    @PostMapping("/register")
    public String performRegistration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        Optional<User> user = userService.findByUsername(userDto.getUsername());

        if (user.isPresent()) {
            result.rejectValue("username", "", "There is already an account registered with the same username");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.save(userDto);
        return "redirect:/register?success";
    }

}