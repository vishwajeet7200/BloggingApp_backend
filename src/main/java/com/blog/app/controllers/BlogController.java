package com.blog.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.blog.app.models.BlogLike;
import com.blog.app.models.Comment;
import com.blog.app.services.BlogService;

@RestController
@RequestMapping("/api/notes")
//@CrossOrigin(origins = "http://localhost:3000")
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	
	@PostMapping
	public Blog createBlog(@RequestBody String content,
			@AuthenticationPrincipal UserDetails userDetails) {
		
		return blogService.createNoteForUser(content, userDetails.getUsername());
		
	}
	
	@GetMapping
	public List<Blog> getUserBlog(@AuthenticationPrincipal UserDetails userDetails) {
		
		return blogService.getNotesForUser(userDetails.getUsername());
	}
	
	@GetMapping("/allnotes")
	public List<Blog> getAllBlog() {
		
		return blogService.getAllNotes();
	}
	
	@DeleteMapping("/{noteId}")
	public void deleteUserBlog(@PathVariable Long noteId,
							   @AuthenticationPrincipal UserDetails userDetails) {
		blogService.deleteNoteForUser(noteId, userDetails.getUsername());
	}
	
	@PutMapping("/{noteId}")
	public Blog updateBlog(@PathVariable Long noteId,
						   @RequestBody String content,
						   @AuthenticationPrincipal UserDetails userDetails) {
		return blogService.updateNotForUser(noteId, content, userDetails.getUsername());
	}
	
	@PostMapping("/{id}/comments")
    public ResponseEntity<Void> addComment(@PathVariable Long id, @RequestBody Comment comment) {
        blogService.addComment(id, comment);
        return ResponseEntity.ok().build();
    }
	
	@GetMapping("/{id}/comments")
    public List<Comment> fetchComment(@PathVariable Long id) {
        
        return blogService.fetchComment(id);
	}

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> addLike(@PathVariable Long id, 
    		@AuthenticationPrincipal UserDetails userDetails) {
    	
        blogService.addLike(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/{id}/like")
    public ResponseEntity<Integer> GetLike(@PathVariable Long id, 
    		@AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(blogService.getTotalLike(id), HttpStatus.OK);
    }
}
