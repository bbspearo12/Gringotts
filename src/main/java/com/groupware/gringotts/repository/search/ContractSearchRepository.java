package com.groupware.gringotts.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.groupware.gringotts.domain.Contract;

/**
 * Spring Data Elasticsearch repository for the Contract entity.
 */
public interface ContractSearchRepository extends ElasticsearchRepository<Contract, Long> {
}
