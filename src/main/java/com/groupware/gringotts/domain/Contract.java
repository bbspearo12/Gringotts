package com.groupware.gringotts.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "contract")
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "contract_number", nullable = false)
    private String contractNumber;

    @NotNull
    @Column(name = "start_of_contract", nullable = false)
    private LocalDate startOfContract;

    @NotNull
    @Column(name = "end_of_contract", nullable = false)
    private LocalDate endOfContract;

    @NotNull
    @Column(name = "coverage_plan", nullable = false)
    private String coveragePlan;

    @OneToOne
    @JoinColumn(unique = true)
    private Company company;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public Contract contractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
        return this;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public LocalDate getStartOfContract() {
        return startOfContract;
    }

    public Contract startOfContract(LocalDate startOfContract) {
        this.startOfContract = startOfContract;
        return this;
    }

    public void setStartOfContract(LocalDate startOfContract) {
        this.startOfContract = startOfContract;
    }

    public LocalDate getEndOfContract() {
        return endOfContract;
    }

    public Contract endOfContract(LocalDate endOfContract) {
        this.endOfContract = endOfContract;
        return this;
    }

    public void setEndOfContract(LocalDate endOfContract) {
        this.endOfContract = endOfContract;
    }

    public String getCoveragePlan() {
        return coveragePlan;
    }

    public Contract coveragePlan(String coveragePlan) {
        this.coveragePlan = coveragePlan;
        return this;
    }

    public void setCoveragePlan(String coveragePlan) {
        this.coveragePlan = coveragePlan;
    }

    public Company getCompany() {
        return company;
    }

    public Contract company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contract contract = (Contract) o;
        if (contract.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, contract.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Contract{" +
            "id=" + id +
            ", contractNumber='" + contractNumber + "'" +
            ", startOfContract='" + startOfContract + "'" +
            ", endOfContract='" + endOfContract + "'" +
            ", coveragePlan='" + coveragePlan + "'" +
            '}';
    }
}
