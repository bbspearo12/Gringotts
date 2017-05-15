package com.groupware.gringotts.repository.search;

import com.groupware.gringotts.domain.Provider;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Provider entity.
 */
public interface ProviderSearchRepository extends ElasticsearchRepository<Provider, Long> {
}
