package com.blog.app.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.app.models.Blog;
import com.blog.app.models.BlogLike;
import com.blog.app.models.User;

@Repository
public interface LikeRepository extends JpaRepository<BlogLike,Long> {

	Optional<BlogLike> findByBlogAndUser(Blog blog, User user);

	List<BlogLike> findByBlogId(Long id);

}
