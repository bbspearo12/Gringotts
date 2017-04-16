package com.groupware.gringotts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupware.gringotts.domain.Contract;

/**
 * Spring Data JPA repository for the Contract entity.
 */
@SuppressWarnings("unused")
public interface ContractRepository extends JpaRepository<Contract,Long> {

}
