package com.kfu.crossplatform.domain;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "username cannot be blank")
    @Size(min = 2, max = 50)
    @Column(unique = true, length = 50)
    private String username;

    @NotBlank(message = "password cannot be blank")
    private String password;

    @NotNull
    private boolean enabled = true;

    @NotNull(message = "role is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<Token> tokens;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<String> authorities = new HashSet<>();
        this.role.getPermissions().forEach(p -> authorities.add(p.getAuthority()));
        authorities.add(role.getAuthority());
        return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()); 
    }
    
}
