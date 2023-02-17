package com.example.demo.persistence;

import com.example.demo.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository  extends JpaRepository<Comment, Long> {

}

