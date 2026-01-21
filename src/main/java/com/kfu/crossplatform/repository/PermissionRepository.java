package com.kfu.crossplatform.repository;

import com.kfu.crossplatform.domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
