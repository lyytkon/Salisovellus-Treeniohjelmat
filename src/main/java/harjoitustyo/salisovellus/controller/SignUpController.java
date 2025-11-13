package harjoitustyo.salisovellus.controller;

import harjoitustyo.salisovellus.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {

    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, 
                        @RequestParam String password,
                        @RequestParam String confirmPassword,
                        Model model) {
        
        // Validointi
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "Käyttäjänimi ei voi olla tyhjä");
            return "signup";
        }
        
        if (password == null || password.length() < 4) {
            model.addAttribute("error", "Salasanan on oltava vähintään 4 merkkiä");
            return "signup";
        }
        
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Salasanat eivät täsmää");
            return "signup";
        }
        
        try {
            userService.registerNewUser(username, password);
            model.addAttribute("success", "Rekisteröinti onnistui! Voit nyt kirjautua sisään.");
            return "login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }
}
