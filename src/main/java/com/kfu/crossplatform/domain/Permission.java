
package com.kfu.crossplatform.domain;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "roles")
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
    @JsonIgnore
    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles;
    @Override
    public String getAuthority(){
        return String.format("%s:%s", resource.toUpperCase(), operation.toUpperCase());
    }
}
