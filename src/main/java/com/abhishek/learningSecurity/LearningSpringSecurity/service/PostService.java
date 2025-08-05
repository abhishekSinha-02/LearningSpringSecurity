package com.abhishek.learningSecurity.LearningSpringSecurity.service;


import com.abhishek.learningSecurity.LearningSpringSecurity.dto.PostDTO;

import java.util.List;

public interface PostService {

    List<PostDTO> getAllPosts();

    PostDTO createNewPost(PostDTO inputPost);

    PostDTO getPostById(Long postId);
}
