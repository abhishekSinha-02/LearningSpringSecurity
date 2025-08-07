package com.abhishek.learningSecurity.LearningSpringSecurity.service.impl;

import com.abhishek.learningSecurity.LearningSpringSecurity.dto.PostDTO;
import com.abhishek.learningSecurity.LearningSpringSecurity.entities.PostEntity;
import com.abhishek.learningSecurity.LearningSpringSecurity.entities.User;
import com.abhishek.learningSecurity.LearningSpringSecurity.exceptions.ResourceNotFoundException;
import com.abhishek.learningSecurity.LearningSpringSecurity.repsoitory.PostRepository;
import com.abhishek.learningSecurity.LearningSpringSecurity.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PostDTO> getAllPosts() {
        return postRepository
                .findAll()
                .stream()
                .map(postEntity -> modelMapper.map(postEntity, PostDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO createNewPost(PostDTO inputPost) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity postEntity = modelMapper.map(inputPost, PostEntity.class);
        postEntity.setAuthor(user);
        return modelMapper.map(postRepository.save(postEntity), PostDTO.class);
    }

    @Override
    public PostDTO getPostById(Long postId) {
        PostEntity postEntity = postRepository
                .findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id "+postId));

        return modelMapper.map(postEntity, PostDTO.class);
    }
}
