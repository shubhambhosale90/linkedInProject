package com.smasher.linkedInProject.postsService.service;

import com.smasher.linkedInProject.postsService.dto.PostCreateRequestDto;
import com.smasher.linkedInProject.postsService.dto.PostDto;
import com.smasher.linkedInProject.postsService.entity.Post;
import com.smasher.linkedInProject.postsService.exception.ResourceNotFoundException;
import com.smasher.linkedInProject.postsService.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {
        log.info("Creating post for user with ID : {}", userId);
        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.info("Getting the post with ID : {}", postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID : "+ postId));
        return modelMapper.map(post, PostDto.class);
    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        log.info("Getting all the posts of a user with ID : {}", userId);
        List<Post> posts = postRepository.findByUserId(userId);
        return posts.stream()
                .map( post -> modelMapper.map(post, PostDto.class))
                .toList();
    }
}
