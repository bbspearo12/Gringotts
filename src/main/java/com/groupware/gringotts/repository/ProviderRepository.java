package com.groupware.gringotts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupware.gringotts.domain.Provider;

/**
 * Spring Data JPA repository for the Provider entity.
 */
@SuppressWarnings("unused")
public interface ProviderRepository extends JpaRepository<Provider,Long> {

}
