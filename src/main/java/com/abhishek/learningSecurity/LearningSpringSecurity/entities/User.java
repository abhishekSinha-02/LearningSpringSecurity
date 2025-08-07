package com.abhishek.learningSecurity.LearningSpringSecurity.entities;

import com.abhishek.learningSecurity.LearningSpringSecurity.entities.enums.Permission;
import com.abhishek.learningSecurity.LearningSpringSecurity.entities.enums.Role;
import com.abhishek.learningSecurity.LearningSpringSecurity.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER) //@ElementCollection will create a separate table user_roles
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @Enumerated(EnumType.STRING)
//    private Set<Permission> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

//        Set<SimpleGrantedAuthority> authorities = roles
//                .stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
//                .collect(Collectors.toSet());

//        permissions.forEach(
//                permission -> authorities.add(new SimpleGrantedAuthority(permission.name()))
//        );

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        roles.forEach(
                role -> {
                    Set<SimpleGrantedAuthority> permissions = PermissionMapping.getAuthoritiesForRole(role);
                    authorities.addAll(permissions);
                    authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
                }
        );
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
