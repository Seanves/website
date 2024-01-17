package demo.security.controllers;

import demo.security.services.PersonService;
import demo.security.entities.Person;
import demo.security.security.PersonDetails;
import demo.security.util.ColorValidator;
import demo.security.util.PersonValidator;
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

    @Autowired private final PersonValidator personValidator;
    @Autowired private final PersonService personService;
//    @Autowired private final ColorValidator colorValidator;


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
    public String register(@ModelAttribute Person person) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()) {
            return "auth/register";
        }
        personService.register(person);
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
//        String error = colorValidator.validate(color);
//        if(error != null) {
//            model.addAttribute("error", error);
//            return "settings";
//        }
        personService.changeColor(getCurrentPerson(), color);
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

//    @GetMapping("/changeYear")
//    public String changeYear(@RequestParam int year) {
//        personService.changeYear(getCurrentPerson(), year);
//        return "hello";
//    }

//    @GetMapping("/changeColor")
//    public String changeYear(@RequestParam String color) {
//        System.out.println("color: " + color);
//        personService.changeColor(getCurrentPerson(), color);
//        return "hello";
//    }


    private Person getCurrentPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails)authentication.getPrincipal();
        return personDetails.getPerson();
    }
}
