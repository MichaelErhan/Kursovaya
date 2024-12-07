package com.example.cargo.controller;

import com.example.cargo.model.BlogPost;
import com.example.cargo.model.User;
import com.example.cargo.service.BlogPostService;
import com.example.cargo.service.UserService;
import com.example.cargo.util.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogPostController {

    private final BlogPostService blogPostService;
    private final UserService userService;

    public BlogPostController(BlogPostService blogPostService, UserService userService) {
        this.blogPostService = blogPostService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showAdminPanel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        List<BlogPost> blogPosts = blogPostService.findAllBlogPosts();
        model.addAttribute("blogPosts", blogPosts);
        return "blog/admin";
    }

    @GetMapping("/add")
    public String showAddBlogPostForm(Model model) {
        model.addAttribute("blogPost", new BlogPost());
        return "redirect:/blog/admin";
    }

    @PostMapping("/add")
    public String addBlogPost(@ModelAttribute BlogPost blogPost, Principal principal) {
        blogPost.setAuthor(userService.findUserByUserName(principal.getName()).orElseThrow());
        blogPostService.saveBlogPost(blogPost);
        return "redirect:/blog/admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditBlogPostForm(@PathVariable Long id, Model model) {
        BlogPost blogPost = blogPostService.findBlogPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blog post Id: " + id));
        model.addAttribute("blogPost", blogPost);
        return "redirect:/blog/admin";
    }

    @PostMapping("/edit")
    public String editBlogPost(@ModelAttribute BlogPost blogPost, Principal principal) {
        blogPost.setAuthor(userService.findUserByUserName(principal.getName()).orElseThrow());
        blogPostService.saveBlogPost(blogPost);
        return "redirect:/blog/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteBlogPost(@PathVariable Long id) {
        blogPostService.deleteBlogPost(id);
        return "redirect:/blog/admin";
    }

    @GetMapping("/")
    public String showBlogHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null && user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MODERATOR)) {
                return "redirect:/blog/admin";
            }
            model.addAttribute("username", user.getUserName());
        }
        List<BlogPost> blogPosts = blogPostService.findAllBlogPosts();
        model.addAttribute("blogPosts", blogPosts);
        return "blog/blog_home";
    }

    @GetMapping("/admin/filter")
    public String searchBlogs(@RequestParam(required = false) String title,
                              @RequestParam(required = false) String publicationDate,
                              @RequestParam(required = false) String author,
                              @RequestParam(required = false) String content,
                              @RequestParam(required = false) String specialist,
                              Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }

        List<BlogPost> blogPosts = blogPostService.searchBlogs(title, publicationDate, author, content, specialist);
        model.addAttribute("blogPosts", blogPosts);
        return "blog/admin";
    }

    @GetMapping("/filter")
    public String searchBlogsUser(@RequestParam(required = false) String title,
                              @RequestParam(required = false) String publicationDate,
                              @RequestParam(required = false) String author,
                              @RequestParam(required = false) String content,
                              @RequestParam(required = false) String specialist,
                              Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }

        List<BlogPost> blogPosts = blogPostService.searchBlogs(title, publicationDate, author, content, specialist);
        model.addAttribute("blogPosts", blogPosts);
        return "/blog/blog_home";
    }
}
