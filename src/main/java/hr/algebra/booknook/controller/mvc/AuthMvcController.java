package hr.algebra.booknook.controller.mvc;

import org.springframework.web.bind.annotation.*;
import hr.algebra.booknook.dto.Dto;
import hr.algebra.booknook.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthMvcController {

    private final AuthService authService;

    private static final String AUTH_REGISTER = "auth/register";

    public AuthMvcController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequest", new Dto.RegisterRequest("", "", ""));
        return AUTH_REGISTER;
    }

    @PostMapping("/register")
    public String register(
        @Valid @ModelAttribute("registerRequest") Dto.RegisterRequest request,
        BindingResult result,
        RedirectAttributes redirectAttributes,
        Model model
    ) {
        if (result.hasErrors()) {
            return AUTH_REGISTER;
        }
        try {
            authService.register(request);
            redirectAttributes.addFlashAttribute("successMessage", "Account created! Please log in.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return AUTH_REGISTER;
        }
    }
}
