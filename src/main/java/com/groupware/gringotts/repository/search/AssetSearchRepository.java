package com.groupware.gringotts.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.groupware.gringotts.domain.Asset;

/**
 * Spring Data Elasticsearch repository for the Asset entity.
 */
public interface AssetSearchRepository extends ElasticsearchRepository<Asset, Long> {
}
