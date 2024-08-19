package com.raze.springExam.controller;

import java.util.ArrayList;

import com.raze.springExam.services.PostService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.raze.springExam.models.Post;
import com.raze.springExam.repos.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class PostController {

    // переменная, которая ссылается на интерфейс (репос)
    @Autowired
    private PostRepository postRepository;

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/blog")
    public String blog(HttpSession session, Model model) {
        String userRole = (String) session.getAttribute("user_role");

        if (userRole == null) {
            // Если переменная отсутствует, перенаправляем на страницу логина
            return "redirect:/user/login";
        } else {
            // массив значений из БД
            Iterable<Post> posts = postRepository.findAll();
            // передаем на страницу массив
            model.addAttribute("posts", posts);
            return "blog-main";
        }
    }

    @GetMapping("/blog/add")
    public String blogAdd(HttpSession session, Model model) {
        String userRole = (String) session.getAttribute("user_role");

        if (userRole == null) {
            // Если переменная отсутствует, перенаправляем на страницу логина
            return "redirect:/user/login";
        }
        return "blog-add";
    }

    @PostMapping("/blog/add")
    public String blogAdded(@RequestParam String title, @RequestParam String description, @RequestParam String full_text, Model model) {
        Post post = new Post(title, description, full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(HttpSession session, @PathVariable(value = "id") long id, Model model) {
        String userRole = (String) session.getAttribute("user_role");

        if (userRole == null) {
            // Если переменная отсутствует, перенаправляем на страницу логина
            return "redirect:/user/login";
        }

        if (!postRepository.existsById(id)) {
            return ("redirect:/blog");
        }

        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        // добавляем в массив "результат" пост
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(HttpSession session, @PathVariable(value = "id") long id, Model model) {
        String userRole = (String) session.getAttribute("user_role");

        if (userRole == null) {
            // Если переменная отсутствует, перенаправляем на страницу логина
            return "redirect:/user/login";
        }

        if (!postRepository.existsById(id)) {
            return ("redirect:/blog");
        }

        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList<>();
        // добавляем в массив "результат" пост
        post.ifPresent(res::add);
        model.addAttribute("post", res);

        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String description, @RequestParam String full_text, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setDescription(description);
        post.setFull_text(full_text);
        postRepository.save(post);

        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/delete")
    public String blogDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);

        return "redirect:/blog";
    }

    @PostMapping("blog/search")
    public String blogSearsch(HttpSession session, @RequestParam("keyword") String keyWord, Model model) {
        if (keyWord != null) {
            Long userId = (Long) session.getAttribute("id");
            Iterable<Post> posts = (Iterable<Post>) postService.searchByTitleOrDescription(keyWord);
            model.addAttribute("posts", posts);
            session.setAttribute("keyword", keyWord);

            return "search-results";
        } else {
            return "redirect:/blog";
        }
    }
}
