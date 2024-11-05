package com.blog.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.app.models.Blog;
import com.blog.app.models.Comment;
import com.blog.app.services.BlogService;

@RestController
@RequestMapping("/api/notes")
//@CrossOrigin(origins = "http://localhost:3000")
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	
	@PostMapping
	public Blog createNote(@RequestBody String content,
			@AuthenticationPrincipal UserDetails userDetails) {
		
		return blogService.createNoteForUser(content, userDetails.getUsername());
		
	}
	
	@GetMapping
	public List<Blog> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
		
		return blogService.getNotesForUser(userDetails.getUsername());
	}
	
	@GetMapping("/allnotes")
	public List<Blog> getAllNotes() {
		
		return blogService.getAllNotes();
	}
	
	@DeleteMapping("/{noteId}")
	public void deleteUserNote(@PathVariable Long noteId,
							   @AuthenticationPrincipal UserDetails userDetails) {
		blogService.deleteNoteForUser(noteId, userDetails.getUsername());
	}
	
	@PutMapping("/{noteId}")
	public Blog updateNote(@PathVariable Long noteId,
						   @RequestBody String content,
						   @AuthenticationPrincipal UserDetails userDetails) {
		return blogService.updateNotForUser(noteId, content, userDetails.getUsername());
	}
	
	@PostMapping("/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable Long id, @RequestBody Comment comment) {
        blogService.addComment(id, comment);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<Void> addLike(@PathVariable Long id, 
    		@AuthenticationPrincipal UserDetails userDetails) {
    	
        blogService.addLike(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
