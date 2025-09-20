package com.smasher.linkedInProject.postsService.service;

import com.smasher.linkedInProject.postsService.entity.Post;
import com.smasher.linkedInProject.postsService.entity.PostLike;
import com.smasher.linkedInProject.postsService.exception.BadRequestException;
import com.smasher.linkedInProject.postsService.exception.ResourceNotFoundException;
import com.smasher.linkedInProject.postsService.repository.PostLikeRepository;
import com.smasher.linkedInProject.postsService.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public void likePost(Long postId) {
        Long userId = 1L;
        log.info("User with ID : {}, Liking the post with ID : {}",userId, postId);

        postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID : "+postId));
        boolean hasAlreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (hasAlreadyLiked) throw new BadRequestException("You can not like the post again");

        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepository.save(postLike);

//        TODO: send notification to the owner of the post
    }

    @Transactional
    public void unlikePost(Long postId) {
        Long userId = 1L;
        log.info("User with ID : {}, Unliking the post with ID : {}",userId, postId);

        postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID : "+postId));
        boolean hasAlreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId, postId);
        if (!hasAlreadyLiked) throw new BadRequestException("You can not unlike the post that you have not liked");

        postLikeRepository.deleteByUserIdAndPostId(userId, postId);
    }
}
