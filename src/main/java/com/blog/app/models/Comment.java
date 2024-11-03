package com.blog.app.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;	
}
