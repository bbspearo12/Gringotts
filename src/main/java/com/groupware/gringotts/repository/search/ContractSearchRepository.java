package com.groupware.gringotts.repository.search;

import com.groupware.gringotts.domain.Contract;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Contract entity.
 */
public interface ContractSearchRepository extends ElasticsearchRepository<Contract, Long> {
}
