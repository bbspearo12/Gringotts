package com.groupware.gringotts.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.groupware.gringotts.domain.User;

/**
 * Spring Data Elasticsearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
}
