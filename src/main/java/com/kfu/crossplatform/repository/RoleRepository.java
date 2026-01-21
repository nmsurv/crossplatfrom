package com.kfu.crossplatform.repository;

import com.kfu.crossplatform.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
