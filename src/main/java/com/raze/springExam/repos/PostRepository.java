package com.raze.springExam.repos;

import com.raze.springExam.models.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {
    
}
