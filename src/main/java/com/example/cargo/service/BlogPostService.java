package com.example.cargo.service;

import com.example.cargo.model.BlogPost;
import com.example.cargo.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository) {
        this.blogPostRepository = blogPostRepository;
    }

    public void saveBlogPost(BlogPost blogPost) {
        blogPostRepository.save(blogPost);
    }

    public Optional<BlogPost> findBlogPostById(Long id) {
        return blogPostRepository.findById(id);
    }

    public List<BlogPost> findAllBlogPosts() {
        return blogPostRepository.findAll();
    }

    public void deleteBlogPost(Long id) {
        blogPostRepository.deleteById(id);
    }

    public List<BlogPost> searchBlogs(String title, String publicationDate, String author, String content, String specialist) {
        return blogPostRepository.searchByQuery(title, publicationDate, author, content, specialist);
    }
}
