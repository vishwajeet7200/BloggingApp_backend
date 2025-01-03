package com.blog.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.blog.app.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

	List<Comment> findByBlogId(Long noteId);


}
