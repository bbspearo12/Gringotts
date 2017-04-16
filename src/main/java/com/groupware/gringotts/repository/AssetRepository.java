package com.groupware.gringotts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.groupware.gringotts.domain.Asset;

/**
 * Spring Data JPA repository for the Asset entity.
 */
@SuppressWarnings("unused")
public interface AssetRepository extends JpaRepository<Asset,Long> {

}
