package com.blog.app.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BlogLike {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
