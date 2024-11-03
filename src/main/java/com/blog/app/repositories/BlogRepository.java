package com.blog.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.blog.app.models.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>{
	List<Blog> findNotesByOwnerUserName(String ownerUsername);
}
