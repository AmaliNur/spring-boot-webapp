package com.example.cargo.controller;

import com.example.cargo.model.BlogPost;
import com.example.cargo.model.User;
import com.example.cargo.repository.BlogPostRepository;
import com.example.cargo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String blog(Model model,
                       @RequestParam(required = false) String keyword,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishDate) {
        List<BlogPost> posts;

        // Если нет параметров поиска, показываем все посты
        if (keyword == null && publishDate == null) {
            posts = blogPostRepository.findAllByOrderByPublishDateDesc();
        } else {
            posts = blogPostRepository.searchBlogPosts(keyword, publishDate);
        }

        model.addAttribute("posts", posts);
        model.addAttribute("keyword", keyword);
        model.addAttribute("publishDate", publishDate);
        return "blog/index";
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {
        model.addAttribute("posts", blogPostRepository.findAllByOrderByPublishDateDesc());
        model.addAttribute("newPost", new BlogPost());
        return "blog/admin";
    }

    @PostMapping("/admin/save")
    public String savePost(@ModelAttribute BlogPost post, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                post.setAuthor(user);
                post.setPublishDate(LocalDate.now());
                blogPostRepository.save(post);
            }
        }
        return "redirect:/blog/admin";
    }

    @GetMapping("/admin/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        BlogPost post = blogPostRepository.findById(id).orElse(null);
        if (post != null) {
            model.addAttribute("post", post);
            return "blog/edit";
        }
        return "redirect:/blog/admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        blogPostRepository.deleteById(id);
        return "redirect:/blog/admin";
    }
}

