package com.groupware.gringotts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupware.gringotts.domain.Company;

/**
 * Spring Data JPA repository for the Company entity.
 */
@SuppressWarnings("unused")
public interface CompanyRepository extends JpaRepository<Company,Long> {

}
