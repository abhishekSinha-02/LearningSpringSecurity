package com.abhishek.learningSecurity.LearningSpringSecurity.repsoitory;

import com.abhishek.learningSecurity.LearningSpringSecurity.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
