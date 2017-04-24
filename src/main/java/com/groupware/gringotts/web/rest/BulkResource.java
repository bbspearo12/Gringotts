package com.groupware.gringotts.web.rest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.groupware.gringotts.domain.Asset;
import com.groupware.gringotts.domain.Company;
import com.groupware.gringotts.domain.Contract;
import com.groupware.gringotts.domain.Provider;
import com.groupware.gringotts.domain.enumeration.AssetType;
import com.groupware.gringotts.repository.AssetRepository;
import com.groupware.gringotts.repository.CompanyRepository;
import com.groupware.gringotts.repository.ContractRepository;
import com.groupware.gringotts.repository.ProviderRepository;
import com.groupware.gringotts.repository.search.AssetSearchRepository;
import com.groupware.gringotts.repository.search.CompanySearchRepository;
import com.groupware.gringotts.repository.search.ContractSearchRepository;
import com.groupware.gringotts.repository.search.ProviderSearchRepository;

@RestController
@RequestMapping("/api")
public class BulkResource {

	private final static Logger log = LoggerFactory.getLogger(BulkResource.class);

    private static final String ENTITY_NAME = "bulk";

//    private final ProviderRepository providerRepository;
//
//    private final ProviderSearchRepository providerSearchRepository;
//
//    public BulkResource(ProviderRepository providerRepository, ProviderSearchRepository providerSearchRepository) {
//        this.providerRepository = providerRepository;
//        this.providerSearchRepository = providerSearchRepository;
//    }


	public static String getProviderName(JSONObject jobj) throws JSONException {
    		return  StringUtils.strip(jobj.getString("Service Vendor"), "Ê");
    }
	public static String getProviderPhone(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("Vendor Contact Number"), "Ê");
	}
	public static String getCompanyName(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("Name"), "Ê");
	}

    // Company Stuff
    public static String getCompanyAL1(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("Address Line 1"), "Ê");
	}
	public static String getCompanyCity(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("City"), "Ê");
	}
	public static String getCompanyState(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("State"), "Ê");
	}
	public static String getCompanyPrimaryPhone(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("Phone Number"), "Ê");
	}
	public static String getCompanyZip(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("Zip"), "Ê");
	}
	public static String getCompanyPrimaryContact(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("Primary Contact"), "Ê");
	}
	public static String getCompanyEmail(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("Email"), "Ê");
	}

	// Contract Stuff
	public static String getContractID(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("Contract"), "Ê");
	}
	public static String getContractStart(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("Start Date"), "Ê");
	}
	public static String getContractEnd(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("End Date"), "Ê");
	}
	public static String getContractCoveragePlan(JSONObject jobj) throws JSONException {
		return StringUtils.strip(jobj.getString("Email"), "Ê");
	}

	// Asset Stuff
    public static String getSerial(JSONObject jobj) throws JSONException {
        return StringUtils.strip(jobj.getString("Serial Number"), "Ê");
    }
    public static String getModel(JSONObject jobj) throws JSONException {
        return StringUtils.strip(jobj.getString("Model"), "Ê");
    }
    public static String getOEM(JSONObject jobj) throws JSONException {
        return StringUtils.strip(jobj.getString("OEM"), "Ê");
    }
    public static String getType(JSONObject jobj) throws JSONException {
        return StringUtils.strip(jobj.getString("Type"), "Ê");
    }


    public static Asset getAssetBySerial(String serial, AssetRepository assetRepository) {
        String trimmedPName = StringUtils.trim(serial);
        log.debug("Searching for is {}", trimmedPName);
        List<Asset> assets = assetRepository.findAll();
        for (Asset p : assets) {
            if (StringUtils.equalsIgnoreCase(trimmedPName, p.getSerialNumber())) {
                return p;
            }
        }
        return null;
    }

    public static Asset createAsset(String assetSerial,
                                    String modelNumber,
                                    String oem,
                                    String type,
                                    Contract co,
                                    Provider p,
                                    AssetRepository assetRepository,
                                    AssetSearchRepository assetSearchRepository) {
        Asset asset = new Asset();
        asset.setSerialNumber(assetSerial);
        asset.setModelNumber(modelNumber);
        asset.setoEM(oem);
        asset.setType(AssetType.valueOf(type.toUpperCase()));
        asset.setContract(co);
        asset.setProvider(p);
    	log.debug("Creating asset with {}", asset);
        Asset result = assetRepository.save(asset);
        assetSearchRepository.save(result);
        return result;
    }

	public static Contract getContractByNumber(String pname, ContractRepository contractRepository) {
    	String trimmedPName = StringUtils.trim(pname);
		log.debug("Searching for is {}", trimmedPName);
    	List<Contract> contracts = contractRepository.findAll();
    	for (Contract p : contracts) {
    		if (StringUtils.equalsIgnoreCase(trimmedPName, p.getContractNumber())) {
    			return p;
    		}
    	}
		return null;
    }

	public static Contract createContract(String contractNumber,
    		String startOfContract,
    		String endOfContract,
    		String coveragePlan,
    		Company c,
    		ContractRepository contractRepository,
    		ContractSearchRepository contractSearchRepository) {
		DateTimeFormatter dtf= null;

		if (startOfContract != null && StringUtils.contains(startOfContract, "/")) {
			dtf = DateTimeFormatter.ofPattern("MM/dd/yy");
			startOfContract = ensureProperDateFormat(startOfContract);
			endOfContract = ensureProperDateFormat(endOfContract);
		} else {
			dtf = DateTimeFormatter.ofPattern("dd MMM yyyy");
		}
		
		LocalDate slocalDate = LocalDate.parse(startOfContract, dtf);
		LocalDate elocalDate = LocalDate.parse(endOfContract, dtf);
		Contract p = new Contract();
	    p.setCompany(c);
	    p.setContractNumber(contractNumber);
	    p.setCoveragePlan(coveragePlan);
	    p.setStartOfContract(slocalDate);
	    p.setEndOfContract(elocalDate);
	    Contract result = contractRepository.save(p);
	    contractSearchRepository.save(result);
	    return result;
    }

	// This method will convert date of the format 7/1/16 to 07/01/16 so that 
	// it can be properly parsed by the formatter.
	public static String ensureProperDateFormat(String unformattedDate) {
		if (unformattedDate == null || StringUtils.countMatches(unformattedDate, "/") != 2) {
			// This not formattable by this method, return as is
			return unformattedDate;
		}
		StringBuilder formattedDate = new StringBuilder();
		String[] toks = unformattedDate.split("/") ;
		if (toks[0].length() == 1) {
			formattedDate.append("0"+toks[0]+"/");
		} else {
			formattedDate.append(toks[0]+"/");
		}
		
		if (toks[1].length() == 1) {
			formattedDate.append("0"+toks[1]+"/");
		} else {
			formattedDate.append(toks[1]+"/");
		}
		formattedDate.append(toks[2]);
		log.debug("Returning formatted date: {}", formattedDate.toString());
		return formattedDate.toString();
	}

    public static Provider getProviderByName(String pname, ProviderRepository providerRepository) {
    	String trimmedPName = StringUtils.trim(pname);
		log.debug("Searching for is {}", trimmedPName);
    	List<Provider> providers = providerRepository.findAll();
    	for (Provider p : providers) {
    		if (StringUtils.equalsIgnoreCase(trimmedPName, p.getProvider())) {
    			return p;
    		}
    	}
		return null;
    }

    public static Provider createProvider(String pname,
    		String phone,
    		String pcontact,
    		ProviderRepository providerRepository,
    		ProviderSearchRepository providerSearchRepository) {
	    Provider p = new Provider();
	    p.setPhone(phone);
	    p.setPrimaryContact(pcontact);
	    p.setProvider(pname);
	    Provider result = providerRepository.save(p);
	    providerSearchRepository.save(result);
	    return result;
    }
    public static Company getCompanyByName(String pname, CompanyRepository companyRepository) {
    	String trimmedPName = StringUtils.trim(pname);
		log.debug("Searching for is {}", trimmedPName);
    	List<Company> cs = companyRepository.findAll();
    	for (Company c : cs) {
    		if (StringUtils.equalsIgnoreCase(trimmedPName,c.getName())) {
    			return c;
    		}
    	}
		return null;
    }
    public static Company createCompany(String cname,
    		String al1,
    		String city,
    		String state,
    		String phno,
    		String zip,
    		String pcontact,
    		String email,
    		CompanyRepository companyRepository,
    		CompanySearchRepository companySearchRepository) {
	    Company c = new Company();
	    c.setName(cname);
	    c.setAddressLine1(al1);
	    c.setCity(city);
	    c.setState(state);
	    c.setPhoneNumber(phno);
	    c.setZip(zip);
	    c.setPrimaryContact(pcontact);
	    c.setEmail(email);

	    Company result = companyRepository.save(c);
	    companySearchRepository.save(result);
	    return result;
    }

}
