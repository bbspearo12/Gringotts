package com.groupware.gringotts.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.groupware.gringotts.domain.Asset;
import com.groupware.gringotts.repository.AssetRepository;
import com.groupware.gringotts.repository.search.AssetSearchRepository;
import com.groupware.gringotts.web.rest.util.PaginationUtil;

import io.swagger.annotations.ApiParam;

/**
 * REST controller for Universal search.
 */
@RestController
@RequestMapping("/api")

public class UniversalSearchResource {
	 private final Logger log = LoggerFactory.getLogger(UniversalSearchResource.class);
	 
	    private final AssetRepository assetRepository;
	    private final AssetSearchRepository assetSearchRepository;
	    public UniversalSearchResource(AssetRepository assetRepository,
	    		AssetSearchRepository assetSearchRepository) {
	        this.assetRepository = assetRepository;
	        this.assetSearchRepository = assetSearchRepository;
	    }
	 @GetMapping("/_search/")
	    @Timed
	    public ResponseEntity<List<Asset>> searchAssets(@RequestParam String query, @ApiParam Pageable pageable) {
	        log.debug("REST request to search for a page of Assets for query {}", query);
	        Page<Asset> page = assetSearchRepository.search(queryStringQuery(query), pageable);
	        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/assets");
	        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	    }
}
