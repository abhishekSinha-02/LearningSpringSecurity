package com.abhishek.learningSecurity.LearningSpringSecurity.utils;

import com.abhishek.learningSecurity.LearningSpringSecurity.dto.PostDTO;
import com.abhishek.learningSecurity.LearningSpringSecurity.entities.PostEntity;
import com.abhishek.learningSecurity.LearningSpringSecurity.entities.User;
import com.abhishek.learningSecurity.LearningSpringSecurity.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSecurityService {

    private final PostService postService;

    public boolean isOwnerOfPost(Long postId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostDTO post = postService.getPostById(postId);
        return post.getAuthor().getId().equals(user.getId());
    }
}
