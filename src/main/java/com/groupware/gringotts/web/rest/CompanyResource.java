package com.groupware.gringotts.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.groupware.gringotts.domain.Asset;
import com.groupware.gringotts.domain.Company;
import com.groupware.gringotts.repository.AssetRepository;
import com.groupware.gringotts.repository.CompanyRepository;
import com.groupware.gringotts.repository.search.CompanySearchRepository;
import com.groupware.gringotts.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Company.
 */
@RestController
@RequestMapping("/api")
public class CompanyResource {

	private final Logger log = LoggerFactory.getLogger(CompanyResource.class);

	private static final String ENTITY_NAME = "company";

	private final CompanyRepository companyRepository;

	private final CompanySearchRepository companySearchRepository;

	private final AssetRepository assetRepository;

	public CompanyResource(CompanyRepository companyRepository, CompanySearchRepository companySearchRepository, AssetRepository assetRepository) {
		this.companyRepository = companyRepository;
		this.companySearchRepository = companySearchRepository;
		this.assetRepository = assetRepository;
	}

	/**
	 * POST  /companies : Create a new company.
	 *
	 * @param company the company to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new company, or with status 400 (Bad Request) if the company has already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/companies")
	@Timed
	public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) throws URISyntaxException {
		log.debug("REST request to save Company : {}", company);
		if (company.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new company cannot already have an ID")).body(null);
		}
		Company result = companyRepository.save(company);
		companySearchRepository.save(result);
		return ResponseEntity.created(new URI("/api/companies/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT  /companies : Updates an existing company.
	 *
	 * @param company the company to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated company,
	 * or with status 400 (Bad Request) if the company is not valid,
	 * or with status 500 (Internal Server Error) if the company couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/companies")
	@Timed
	public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) throws URISyntaxException {
		log.debug("REST request to update Company : {}", company);
		if (company.getId() == null) {
			return createCompany(company);
		}
		Company result = companyRepository.save(company);
		companySearchRepository.save(result);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, company.getId().toString()))
				.body(result);
	}

	/**
	 * GET  /companies : get all the companies.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of companies in body
	 */
	@GetMapping("/companies")
	@Timed
	public List<Company> getAllCompanies() {
		log.debug("REST request to get all Companies");
		List<Company> companies = companyRepository.findAll();
		return companies;
	}

	/**
	 * GET  /companies/:id : get the "id" company.
	 *
	 * @param id the id of the company to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the company, or with status 404 (Not Found)
	 */
	@GetMapping("/companies/{id}")
	@Timed
	public ResponseEntity<Company> getCompany(@PathVariable Long id) {
		log.debug("REST request to get Company : {}", id);
		Company company = companyRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(company));
	}

	/**
	 * GET  /companies/:id/assets : get all assets for the "id" company.
	 *
	 * @param id the id of the company for which assets have to be retrieve
	 * @return the ResponseEntity with status 200 (OK) and with list of assets for the company, or with status 404 (Not Found)
	 * @throws IOException 
	 * @throws JSONException 
	 */
	@GetMapping("/companies/{id}/assets")
	@Timed
	public void getCompanyAssets(@PathVariable Long id, final HttpServletResponse response) throws IOException, JSONException {
		log.debug("REST request to get assets for Company : {}", id);
		String filename = id+".csv";
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				filename);
		response.setHeader(headerKey, headerValue);

		JSONArray ja = new JSONArray();
		Iterator<Asset> assets = assetRepository.findAll().iterator();
		int i=0;
		while (assets.hasNext()) {
			Asset a = assets.next();
			if (a.getContract().getCompany().getId() != id) {
				// skip this asset
				continue;
			}
			try {
				JSONObject formattedjo = getCSVFormattedJSONObject(a);
				ja.put(i, formattedjo);
				i++;
			} catch (JSONException e) {
				log.warn("Failed to unmarshal: {}", a);
				continue;
			}

		}
		log.debug("Returning json array of assets: {}", ja);
		String csv = CDL.toString(ja);
		response.getOutputStream().write(csv.getBytes(Charset.forName("UTF-8")));
	}



	/**
	 * 
	 * @param Asset a
	 * @return Json formatted as below
	 * OEM	Model	Serial Number	Type	Contract	Name	Address Line 1	City	State	Zip	Primary Contact	
	 * Phone Number	Email	Start Date	End Date	Coverage Plan	
	 * Service Vendor	Vendor Primary Contact	Vendor Contact Number	Vendor Email
	 * @throws JSONException 
	 */
	private static JSONObject getCSVFormattedJSONObject(Asset a) throws JSONException {
		LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();

		lhm.put("OEM", a.getoEM());
		lhm.put("Model", a.getModelNumber());
		lhm.put("Serial Number", a.getSerialNumber());
		lhm.put("Type", a.getType().toString());
		lhm.put("Contract", a.getContract().getContractNumber());
		lhm.put("Name", a.getContract().getCompany().getName());
		lhm.put("Address Lane 1", a.getContract().getCompany().getAddressLine1());
		lhm.put("City", a.getContract().getCompany().getCity());
		lhm.put("State", a.getContract().getCompany().getState());
		lhm.put("Zip", a.getContract().getCompany().getZip());
		lhm.put("Primary Contact", a.getContract().getCompany().getPrimaryContact());
		lhm.put("Start Date", a.getContract().getStartOfContract().toString());
		lhm.put("End Date", a.getContract().getEndOfContract().toString());
		lhm.put("Coverage Plan", a.getContract().getCoveragePlan());
		lhm.put("Service Vendor", a.getProvider().getProvider());
		lhm.put("Vendor Primary Contact", a.getProvider().getPrimaryContact());
		lhm.put("Vendor Contact Number", a.getProvider().getPhone());
		// todo update this after the provider entity gets email field
		lhm.put("Vendor Email", "");
		JSONObject  jo = new JSONObject(lhm);
		return jo;
	}
	/**
	 * DELETE  /companies/:id : delete the "id" company.
	 *
	 * @param id the id of the company to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/companies/{id}")
	@Timed
	public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
		log.debug("REST request to delete Company : {}", id);
		companyRepository.delete(id);
		companySearchRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	/**
	 * SEARCH  /_search/companies?query=:query : search for the company corresponding
	 * to the query.
	 *
	 * @param query the query of the company search 
	 * @return the result of the search
	 */
	@GetMapping("/_search/companies")
	@Timed
	public List<Company> searchCompanies(@RequestParam String query) {
		log.debug("REST request to search Companies for query {}", query);
		return StreamSupport
				.stream(companySearchRepository.search(queryStringQuery(query)).spliterator(), false)
				.collect(Collectors.toList());
	}


}
