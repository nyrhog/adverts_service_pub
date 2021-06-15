package com.project.dao;

import com.project.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> getCommentByIdAndProfile_User_Username(Long id, String username);
}
