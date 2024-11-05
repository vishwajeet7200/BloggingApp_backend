package com.blog.app.services;

import java.util.List;

import com.blog.app.models.Blog;
import com.blog.app.models.Comment;

public interface BlogService {
	
	Blog createNoteForUser(String usernmae,String content);
	Blog updateNotForUser(Long noteId, String content, String username);
	void deleteNoteForUser(Long noteId,String username);
	List<Blog> getNotesForUser(String username);
	void addComment(Long blogId, Comment comment);
	void addLike(Long blogId, String username);
	List<Blog> getAllNotes();
	
}
