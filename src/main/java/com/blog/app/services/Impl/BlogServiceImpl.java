package com.blog.app.services.Impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.app.models.Blog;
import com.blog.app.models.Comment;
import com.blog.app.models.BlogLike;
import com.blog.app.models.User;
import com.blog.app.repositories.BlogRepository;
import com.blog.app.repositories.CommentRepository;
import com.blog.app.repositories.LikeRepository;
import com.blog.app.repositories.UserRepository;
import com.blog.app.services.BlogService;

@Service
public class BlogServiceImpl implements BlogService {
	
	@Autowired
	private BlogRepository blogRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
    private LikeRepository likeRepository;
	
    @Autowired
    private UserRepository userRepository;

	@Override
	public Blog createNoteForUser(String content, String usernmae) {
		
		Blog blog=new Blog();
		blog.setContent(content);
		blog.setOwnerUserName(usernmae);
		return blogRepository.save(blog);
		
	}

	@Override
	public Blog updateNotForUser(Long noteId, String content, String username) {
		
		Blog blog=blogRepository
				.findById(noteId)
				.orElseThrow(() 
						-> new RuntimeException("Note not found"));
		blog.setContent(content);
		return blogRepository.save(blog);
		
	}

	@Override
	public void deleteNoteForUser(Long noteId, String username) {
		
		blogRepository.deleteById(noteId);
		
	}

	@Override
	public List<Blog> getNotesForUser(String username) {
		
		//List<Note> notes=noteRepository.findNotesByUsername(username);
		return blogRepository.findNotesByOwnerUserName(username);
		
	}
	
	@Override
	public void addComment(Long blogId, Comment comment) {
        Blog blog = blogRepository.findById(blogId).orElseThrow();
        comment.setBlog(blog);
        commentRepository.save(comment);
    }
	
	@Override
    public void addLike(Long blogId, String username) {
        Blog blog = blogRepository.findById(blogId).orElseThrow();
        User user = userRepository.findByUserName(username).orElseThrow();

        // Check if the user already liked this blog
        Optional<BlogLike> existingLike = likeRepository.findByBlogAndUser(blog, user);
        if (existingLike.isPresent()) {
            throw new RuntimeException("User has already liked this blog.");
        }

        BlogLike blogLike = new BlogLike();
        blogLike.setBlog(blog);
        blogLike.setUser(user);
        likeRepository.save(blogLike);
    }
	
}
