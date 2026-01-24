
package com.kfu.crossplatform.domain;

import java.util.Set;

import org.springframework.cglib.core.Local;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "permissions")
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String resource;
    private String operation;
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
    @Override
    public String getAuthority(){
        return String.format("%s:%", resource.toUpperCase(), operation.toUpperCase());
    }
}
