package com.groupware.gringotts.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import com.groupware.gringotts.domain.enumeration.AssetType;

/**
 * A Asset.
 */
@Entity
@Table(name = "asset")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "asset")
public class Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "o_em")
    private String oEM;

    @Column(name = "model_number")
    private String modelNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AssetType type;

    @OneToOne
    @JoinColumn
    private Contract contract;

    @OneToOne
    @JoinColumn
    private Provider provider;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public Asset serialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getoEM() {
        return oEM;
    }

    public Asset oEM(String oEM) {
        this.oEM = oEM;
        return this;
    }

    public void setoEM(String oEM) {
        this.oEM = oEM;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public Asset modelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
        return this;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public AssetType getType() {
        return type;
    }

    public Asset type(AssetType type) {
        this.type = type;
        return this;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public Contract getContract() {
        return contract;
    }

    public Asset contract(Contract contract) {
        this.contract = contract;
        return this;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Provider getProvider() {
        return provider;
    }

    public Asset provider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Asset asset = (Asset) o;
        if (asset.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, asset.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Asset{" +
            "id=" + id +
            ", serialNumber='" + serialNumber + "'" +
            ", oEM='" + oEM + "'" +
            ", modelNumber='" + modelNumber + "'" +
            ", type='" + type + "'" +
            '}';
    }
}
