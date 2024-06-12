package demo.security.controllers;

import demo.security.entities.User;
import demo.security.entities.dto.PasswordChangeDTO;
import demo.security.entities.dto.UserDTO;
import demo.security.services.UserService;
import demo.security.security.UserDetailsImpl;
import jakarta.validation.Valid;

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
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String mainPage(@RequestParam(required = false) String name, Model model) {
        model.addAttribute("name", name == null ? getCurrentUser().getUsername() : name);
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
    public String settings(@ModelAttribute PasswordChangeDTO passwordChangeDTO) {
        return "settings";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @PostMapping("/changeColor")
    public String changeColor(@RequestParam String color, Model model) {
        userService.changeColor(getCurrentUser(), color);
        return "redirect:/settings";
    }

    @PostMapping("/changePassword")
    public String changePassword(@ModelAttribute @Valid PasswordChangeDTO passwordChangeDTO,
                                                                BindingResult br, Model model) {
        boolean success = userService.changePassword(getCurrentUser(), passwordChangeDTO, br);

        model.addAttribute("passwordChangedSuccessfully", success);
        return "settings";
    }



    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl)authentication.getPrincipal();
        return userDetailsImpl.getUser();
    }
}
