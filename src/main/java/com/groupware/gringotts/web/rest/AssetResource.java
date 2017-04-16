package com.groupware.gringotts.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
import com.groupware.gringotts.domain.Asset;
import com.groupware.gringotts.domain.Company;
import com.groupware.gringotts.domain.Contract;
import com.groupware.gringotts.domain.Provider;
import com.groupware.gringotts.repository.AssetRepository;
import com.groupware.gringotts.repository.CompanyRepository;
import com.groupware.gringotts.repository.ContractRepository;
import com.groupware.gringotts.repository.ProviderRepository;
import com.groupware.gringotts.repository.search.AssetSearchRepository;
import com.groupware.gringotts.repository.search.CompanySearchRepository;
import com.groupware.gringotts.repository.search.ContractSearchRepository;
import com.groupware.gringotts.repository.search.ProviderSearchRepository;
import com.groupware.gringotts.web.rest.util.HeaderUtil;
import com.groupware.gringotts.web.rest.util.PaginationUtil;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Asset.
 */
@RestController
@RequestMapping("/api")
public class AssetResource {

    private final Logger log = LoggerFactory.getLogger(AssetResource.class);

    private static final String ENTITY_NAME = "asset";

    private final AssetRepository assetRepository;

    private final AssetSearchRepository assetSearchRepository;

    // TODO remove from here
    private final ProviderRepository providerRepository;
    private final ProviderSearchRepository providerSearchRepository;
    private final CompanyRepository companyRepository;
    private final CompanySearchRepository companySearchRepository;
    private final ContractRepository contractRepository;
    private final ContractSearchRepository contractSearchRepository;

    //TODO to here

    public AssetResource(AssetRepository assetRepository,
    		AssetSearchRepository assetSearchRepository,
    		ProviderRepository providerRepository,
    		ProviderSearchRepository providerSearchRepository,
    		CompanyRepository companyRepository,
    		CompanySearchRepository companySearchRepository,
    		ContractRepository contractRepository,
    		ContractSearchRepository contractSearchRepository) {
        this.assetRepository = assetRepository;
        this.assetSearchRepository = assetSearchRepository;
        this.providerRepository = providerRepository;
        this.providerSearchRepository = providerSearchRepository;
        this.companyRepository = companyRepository;
        this.companySearchRepository = companySearchRepository;
        this.contractRepository = contractRepository;
        this.contractSearchRepository = contractSearchRepository;
    }

    /**
     * POST  /assets : Create a new asset.
     *
     * @param asset the asset to create
     * @return the ResponseEntity with status 201 (Created) and with body the new asset, or with status 400 (Bad Request) if the asset has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/assets")
    @Timed
    public ResponseEntity<Asset> createAsset(@Valid @RequestBody Asset asset) throws URISyntaxException {
        log.debug("REST request to save Asset : {}", asset);
        if (asset.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new asset cannot already have an ID")).body(null);
        }
        Asset result = assetRepository.save(asset);
        assetSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/assets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /assets : Updates an existing asset.
     *
     * @param asset the asset to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated asset,
     * or with status 400 (Bad Request) if the asset is not valid,
     * or with status 500 (Internal Server Error) if the asset couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/assets")
    @Timed
    public ResponseEntity<Asset> updateAsset(@Valid @RequestBody Asset asset) throws URISyntaxException {
        log.debug("REST request to update Asset : {}", asset);
        if (asset.getId() == null) {
            return createAsset(asset);
        }
        Asset result = assetRepository.save(asset);
        assetSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, asset.getId().toString()))
            .body(result);
    }

    /**
     * GET  /assets : get all the assets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of assets in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/assets")
    @Timed
    public ResponseEntity<List<Asset>> getAllAssets(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Assets");
        Page<Asset> page = assetRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/assets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /assets/:id : get the "id" asset.
     *
     * @param id the id of the asset to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the asset, or with status 404 (Not Found)
     */
    @GetMapping("/assets/{id}")
    @Timed
    public ResponseEntity<Asset> getAsset(@PathVariable Long id) {
        log.debug("REST request to get Asset : {}", id);
        Asset asset = assetRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(asset));
    }

    /**
     * DELETE  /assets/:id : delete the "id" asset.
     *
     * @param id the id of the asset to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/assets/{id}")
    @Timed
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        log.debug("REST request to delete Asset : {}", id);
        assetRepository.delete(id);
        assetSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/assets?query=:query : search for the asset corresponding
     * to the query.
     *
     * @param query the query of the asset search
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/assets")
    @Timed
    public ResponseEntity<List<Asset>> searchAssets(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Assets for query {}", query);
        Page<Asset> page = assetSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/assets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/bulk/upload")
    @Timed
    public ResponseEntity bulkUpload(@RequestBody String json) throws URISyntaxException, JSONException {
        log.debug("REST request to bulk uplaod : {}", json);
    	JSONObject reponse = new JSONObject();
        try {
        	JSONArray jarray =  new JSONArray(json);
			if (jarray.length() == 0) {
	            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "EmptyBody", "A bulk upload cannot empyt body")).body("A bulk upload cannot empty body");
			}
			 log.debug("Got: {}", jarray);
			 for (int i=0; i<jarray.length(); i++) {
				 JSONObject jobj = (JSONObject) jarray.get(i);
				 String assetSerial = BulkResource.getSerial(jobj);
				 if (assetSerial == null || assetSerial.length() == 0) {
					 log.debug("This line is empty, skipping {}", jobj);
					 continue;
				 }
				 Provider p = getOrCreateProvider(jobj);
				 log.debug("provider id {}", p.getId());
				 Company c = getOrCreateCompany(jobj);
				 log.debug("company id {}", c.getId());
				 Contract co = getOrCreateContract(jobj, c);
                 log.debug("contract id {}", co.getId());
                 Asset a = getOrCreateAsset(jobj, co, p);
                 log.debug("asset serial {}", a.getSerialNumber());
			 }
		} catch (Exception e) {
			log.error(e.getMessage());
			reponse.put("Err", "Failed to parse input. "+e.getMessage());
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "InvalidBody", "Failed to parse input. Err: "+e.getMessage())).body(reponse.toString());
		}
        reponse.put("OK", "Saved Bulk Upload");
        return ResponseEntity.ok().body(reponse.toString());
    }


    public Asset getOrCreateAsset(JSONObject jobj, Contract co, Provider p) throws JSONException {
        String assetSerial = BulkResource.getSerial(jobj);
        log.debug("Asset serial is {}", assetSerial);
        Asset asset = BulkResource.getAssetBySerial(assetSerial, assetRepository);
        if (asset == null) {
            log.debug("Need to create Asset {}", assetSerial);
            String modelNumber = BulkResource.getModel(jobj);
            String oem = BulkResource.getOEM(jobj);
            String type = BulkResource.getType(jobj);
            asset = BulkResource.createAsset(assetSerial,
                modelNumber,
                oem,
                type,
                co,
                p,
                assetRepository,
                assetSearchRepository);
        }
        return asset;
    }

    public Contract getOrCreateContract(JSONObject jobj, Company c) throws JSONException {
    	String contractid = BulkResource.getContractID(jobj);
		 log.debug("contract is {}", contractid);
		 Contract p = BulkResource.getContractByNumber(contractid, contractRepository);
		 if (p == null) {
			 log.debug("Need to create contract {}", contractid);
			 String startDate = BulkResource.getContractStart(jobj);
             String endDate = BulkResource.getContractEnd(jobj);
             String coveragePlan = BulkResource.getContractCoveragePlan(jobj);
			 p = BulkResource.createContract(contractid,
                 startDate,
                 endDate,
                 coveragePlan,
                 c,
                 this.contractRepository,
                 this.contractSearchRepository);
		 }
		 return p;
    }


    public Provider getOrCreateProvider(JSONObject jobj) throws JSONException {
    	String sv = BulkResource.getProviderName(jobj);
		 log.debug("sv is {}", sv);
		 Provider p = BulkResource.getProviderByName(sv, this.providerRepository);
		 if (p == null) {
			 log.debug("Need to create sv {}", sv);
			 String phone = BulkResource.getProviderPhone(jobj);
			 p = BulkResource.createProvider(sv, phone, phone, this.providerRepository, this.providerSearchRepository);
		 }
		 return p;
    }

    public Company getOrCreateCompany(JSONObject jobj) throws JSONException {
    	String cname = BulkResource.getCompanyName(jobj);
		 log.debug("cname is {}", cname);
		 Company c = BulkResource.getCompanyByName(cname, companyRepository);
		 if (c == null) {
			 log.debug("Need to create company {}", cname);
			 String al1 = BulkResource.getCompanyAL1(jobj);
			 String city = BulkResource.getCompanyCity(jobj);
			 String state = BulkResource.getCompanyState(jobj);
			 String phno = BulkResource.getCompanyPrimaryPhone(jobj);
			 String zip = BulkResource.getCompanyZip(jobj);
			 String pcontact = BulkResource.getCompanyPrimaryContact(jobj);
			 String email = BulkResource.getCompanyEmail(jobj);
			 c = BulkResource.createCompany(cname,
					 al1,
					 city,
					 state,
					 phno,
					 zip,
					 pcontact,
					 email,
					 companyRepository, companySearchRepository);
		 }
		 return c;
    }

}
