package com.groupware.gringotts.service;

import com.codahale.metrics.annotation.Timed;
import com.groupware.gringotts.domain.*;
import com.groupware.gringotts.repository.*;
import com.groupware.gringotts.repository.search.*;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

@Service
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    private final AssetRepository assetRepository;

    private final AssetSearchRepository assetSearchRepository;

    private final CompanyRepository companyRepository;

    private final CompanySearchRepository companySearchRepository;

    private final ContractRepository contractRepository;

    private final ContractSearchRepository contractSearchRepository;

    private final ProviderRepository providerRepository;

    private final ProviderSearchRepository providerSearchRepository;

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ElasticsearchIndexService(
        UserRepository userRepository,
        UserSearchRepository userSearchRepository,
        AssetRepository assetRepository,
        AssetSearchRepository assetSearchRepository,
        CompanyRepository companyRepository,
        CompanySearchRepository companySearchRepository,
        ContractRepository contractRepository,
        ContractSearchRepository contractSearchRepository,
        ProviderRepository providerRepository,
        ProviderSearchRepository providerSearchRepository,
        ElasticsearchTemplate elasticsearchTemplate) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.assetRepository = assetRepository;
        this.assetSearchRepository = assetSearchRepository;
        this.companyRepository = companyRepository;
        this.companySearchRepository = companySearchRepository;
        this.contractRepository = contractRepository;
        this.contractSearchRepository = contractSearchRepository;
        this.providerRepository = providerRepository;
        this.providerSearchRepository = providerSearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Async
    @Timed
    public void reindexAll() {
        reindexForClass(Asset.class, assetRepository, assetSearchRepository);
        reindexForClass(Company.class, companyRepository, companySearchRepository);
        reindexForClass(Contract.class, contractRepository, contractSearchRepository);
        reindexForClass(Provider.class, providerRepository, providerSearchRepository);
        reindexForClass(User.class, userRepository, userSearchRepository);

        log.info("Elasticsearch: Successfully performed reindexing");
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
                                                              ElasticsearchRepository<T, ID> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            try {
                Method m = jpaRepository.getClass().getMethod("findAllWithEagerRelationships");
                elasticsearchRepository.save((List<T>) m.invoke(jpaRepository));
            } catch (Exception e) {
                elasticsearchRepository.save(jpaRepository.findAll());
            }
        }
        log.info("Elasticsearch: Indexed all rows for " + entityClass.getSimpleName());
    }
}
