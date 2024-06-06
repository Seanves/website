package demo.security.controllers;

import demo.security.entities.User;
import demo.security.entities.dto.UserDTO;
import demo.security.services.UserService;
import demo.security.security.UserDetailsImpl;
import demo.security.util.UserDTOValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired private final UserDTOValidator userDTOValidator;
    @Autowired private final UserService userService;


    @GetMapping
    public String mainPage(@RequestParam(required = false) String name, Model model) {
        model.addAttribute("name", name == null ? getCurrentPerson().getUsername() : name);
        return "main";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(@ModelAttribute UserDTO userDTO) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid UserDTO userDTO, BindingResult bindingResult) {
        userDTOValidator.validate(userDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            return "auth/register";
        }
        userService.register(userDTO);
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "auth/logout";
    }

    @GetMapping("/error")
    public String error(String text, Model model) {
        model.addAttribute("error", text);
        return "error";
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }

    @PostMapping("/changeColor")
    public String changeColor(@RequestParam String color, Model model) {
        userService.changeColor(getCurrentPerson(), color);
        return "settings";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/printPerson")
    public String printPerson() {
        System.out.println(getCurrentPerson());
        return "hello";
    }


    private User getCurrentPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl)authentication.getPrincipal();
        return userDetailsImpl.getPerson();
    }
}
