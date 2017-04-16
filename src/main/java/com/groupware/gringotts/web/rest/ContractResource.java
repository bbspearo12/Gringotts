package com.groupware.gringotts.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.groupware.gringotts.domain.Contract;
import com.groupware.gringotts.repository.ContractRepository;
import com.groupware.gringotts.repository.search.ContractSearchRepository;
import com.groupware.gringotts.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Contract.
 */
@RestController
@RequestMapping("/api")
public class ContractResource {

    private final Logger log = LoggerFactory.getLogger(ContractResource.class);

    private static final String ENTITY_NAME = "contract";
        
    private final ContractRepository contractRepository;

    private final ContractSearchRepository contractSearchRepository;

    public ContractResource(ContractRepository contractRepository, ContractSearchRepository contractSearchRepository) {
        this.contractRepository = contractRepository;
        this.contractSearchRepository = contractSearchRepository;
    }

    /**
     * POST  /contracts : Create a new contract.
     *
     * @param contract the contract to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contract, or with status 400 (Bad Request) if the contract has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/contracts")
    @Timed
    public ResponseEntity<Contract> createContract(@Valid @RequestBody Contract contract) throws URISyntaxException {
        log.debug("REST request to save Contract : {}", contract);
        if (contract.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new contract cannot already have an ID")).body(null);
        }
        Contract result = contractRepository.save(contract);
        contractSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contracts : Updates an existing contract.
     *
     * @param contract the contract to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contract,
     * or with status 400 (Bad Request) if the contract is not valid,
     * or with status 500 (Internal Server Error) if the contract couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/contracts")
    @Timed
    public ResponseEntity<Contract> updateContract(@Valid @RequestBody Contract contract) throws URISyntaxException {
        log.debug("REST request to update Contract : {}", contract);
        if (contract.getId() == null) {
            return createContract(contract);
        }
        Contract result = contractRepository.save(contract);
        contractSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, contract.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contracts : get all the contracts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contracts in body
     */
    @GetMapping("/contracts")
    @Timed
    public List<Contract> getAllContracts() {
        log.debug("REST request to get all Contracts");
        List<Contract> contracts = contractRepository.findAll();
        return contracts;
    }

    /**
     * GET  /contracts/:id : get the "id" contract.
     *
     * @param id the id of the contract to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contract, or with status 404 (Not Found)
     */
    @GetMapping("/contracts/{id}")
    @Timed
    public ResponseEntity<Contract> getContract(@PathVariable Long id) {
        log.debug("REST request to get Contract : {}", id);
        Contract contract = contractRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(contract));
    }

    /**
     * DELETE  /contracts/:id : delete the "id" contract.
     *
     * @param id the id of the contract to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/contracts/{id}")
    @Timed
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        log.debug("REST request to delete Contract : {}", id);
        contractRepository.delete(id);
        contractSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/contracts?query=:query : search for the contract corresponding
     * to the query.
     *
     * @param query the query of the contract search 
     * @return the result of the search
     */
    @GetMapping("/_search/contracts")
    @Timed
    public List<Contract> searchContracts(@RequestParam String query) {
        log.debug("REST request to search Contracts for query {}", query);
        return StreamSupport
            .stream(contractSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
