package com.nike.nike.Controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.nike.nike.ProductRepository;
import com.nike.nike.UsersService;
import com.nike.nike.Models.Product;
import com.nike.nike.Models.Users;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MainController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping("/")
    public String index(Model model) {
        List <Product> products = productRepository.findAll();
        model.addAttribute("products", products);
        return "index";
    }
    @PostMapping("/login")
       public String login(String email, String password, HttpSession session, Model model) {
        String loginResult = usersService.loginUser(email, password, session);

        if ("User not found".equals(loginResult) || "Invalid password".equals(loginResult)) {
            model.addAttribute("error", loginResult);
            return "login";
        }

        return "redirect:products"; // Redirect to dashboard based on role
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(@Valid Users user, Model model) {
    String result = usersService.registerUser(user);
    
    if (result.equals("Email is already registered!")) {
        // Add error message to the model to display on the registration page
        model.addAttribute("errorMessage", result);
        return "register"; // Return the registration page with the error message
    }
    
    // Redirect to the login page after successful registration
    return "redirect:/login";
}

    @GetMapping("/register")
    public String register() {
        return "register";
    }
    @GetMapping("/users-mgmt")
    public String listUsers(Model model) {
        model.addAttribute("users", usersService.findAll());
        return "usermgmt";
    }
    @GetMapping("users-mgmt/edit/{id}")
    public String editUserForm(@PathVariable Integer id, Model model) {
        Users user = usersService.findById(id);
        model.addAttribute("user", user);
        return "modifyuser";
    }
    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute Users user) {
    usersService.save(user); // Save the updated user details
    return "redirect:/users-mgmt"; // Redirect back to the users list page
}
    @GetMapping("users-mgmt/delete/{id}")
    public String deleteUser(@PathVariable Integer id) {
        usersService.delete(id);
        return "usermgmt";
    }
    
    
}

