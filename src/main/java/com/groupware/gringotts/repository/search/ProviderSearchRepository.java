package com.groupware.gringotts.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.groupware.gringotts.domain.Provider;

/**
 * Spring Data Elasticsearch repository for the Provider entity.
 */
public interface ProviderSearchRepository extends ElasticsearchRepository<Provider, Long> {
}
