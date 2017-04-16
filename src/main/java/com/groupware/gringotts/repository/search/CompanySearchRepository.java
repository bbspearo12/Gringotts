package com.groupware.gringotts.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.groupware.gringotts.domain.Company;

/**
 * Spring Data Elasticsearch repository for the Company entity.
 */
public interface CompanySearchRepository extends ElasticsearchRepository<Company, Long> {
}
