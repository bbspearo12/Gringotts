package com.groupware.gringotts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupware.gringotts.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
