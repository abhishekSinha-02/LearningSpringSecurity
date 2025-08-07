package com.abhishek.learningSecurity.LearningSpringSecurity.repsoitory;

import com.abhishek.learningSecurity.LearningSpringSecurity.entities.Session;
import com.abhishek.learningSecurity.LearningSpringSecurity.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByUser(User user);

    Optional<Session> findByRefreshToken(String refreshToken);
}