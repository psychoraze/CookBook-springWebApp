package com.raze.springExam.services;

import com.raze.springExam.models.Post;
import com.raze.springExam.repos.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> searchByTitleOrDescription(String keyword) {
        List<Post> allPosts = (List<Post>) postRepository.findAll();
        return allPosts.stream()
                .filter(post -> post.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                        post.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}
