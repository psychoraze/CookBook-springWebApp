package com.raze.springExam.controller;

import com.raze.springExam.models.Post;
import com.raze.springExam.models.User;
import com.raze.springExam.repos.UserRepository;
import com.raze.springExam.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("user/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("user/login")
    public String loginSuccess(@RequestParam("email") String email, HttpSession session, Model model) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String role = user.getRole();
            Long id = user.getId();
            // Сохраняем роль в сессии
            session.setAttribute("user_role", role);
            session.setAttribute("id", id);
        }

        return "redirect:/";
    }

    @GetMapping("user/registr")
    public String register(Model model) {
        return "registr";
    }

    @PostMapping("user/registr")
    public String registerSuccess(@RequestParam String username, @RequestParam String email, @RequestParam String phone_number, @RequestParam String password, @RequestParam String role, Model model) {
        User user = new User(username, email, phone_number, password, role);
        userRepository.save(user);

        return "redirect:/";
    }

    @GetMapping("user/logout")
    public String logout(HttpSession session, Model model) {
        session.removeAttribute("user_role");

        return "redirect:/";
    }

    @GetMapping("user/list")
    public String usersList(HttpSession session, Model model) {
        String userRole = (String) session.getAttribute("user_role");

        if (userRole == null) {
            // Если переменная отсутствует, перенаправляем на страницу логина
            return "redirect:/user/login";
        } else {
            // массив значений из БД
            Long curentAdminId = (Long) session.getAttribute("id");  // Получаем ID пользователя из сессии
            Iterable<User> users = userService.getAllUsersExcludingCurrent(curentAdminId);
            // передаем на страницу массив
            model.addAttribute("users", users);
            return "users-list";
        }
    }

    @GetMapping("/user/{id}/delete")
    public String userDelete(@PathVariable(value = "id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);

        return "redirect:/user/list";
    }
}
