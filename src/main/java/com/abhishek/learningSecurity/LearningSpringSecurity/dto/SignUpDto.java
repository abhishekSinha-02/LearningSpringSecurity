package com.abhishek.learningSecurity.LearningSpringSecurity.dto;

import com.abhishek.learningSecurity.LearningSpringSecurity.entities.enums.Permission;
import com.abhishek.learningSecurity.LearningSpringSecurity.entities.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto {
    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
    private Set<Permission> permissions;
}
