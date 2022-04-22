package com.clap.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A Company.
 */
@Node
public class Company extends User {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    private String id;

    @Property("company_name")
    private String companyName;

    @Property("tax_id_number")
    private String taxIdNumber;

    @Property("office_address")
    private String officeAddress;

    @Relationship("HAS_PRIVACY_REQUEST")
    @JsonIgnoreProperties(value = { "company", "contentCreatto" }, allowSetters = true)
    private Set<PrivacyRequest> privacyRequests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Company id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Company companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaxIdNumber() {
        return this.taxIdNumber;
    }

    public Company taxIdNumber(String taxIdNumber) {
        this.setTaxIdNumber(taxIdNumber);
        return this;
    }

    public void setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
    }

    public String getOfficeAddress() {
        return this.officeAddress;
    }

    public Company officeAddress(String officeAddress) {
        this.setOfficeAddress(officeAddress);
        return this;
    }

    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    public Set<PrivacyRequest> getPrivacyRequests() {
        return this.privacyRequests;
    }

    public void setPrivacyRequests(Set<PrivacyRequest> privacyRequests) {
        if (this.privacyRequests != null) {
            this.privacyRequests.forEach(i -> i.setCompany(null));
        }
        if (privacyRequests != null) {
            privacyRequests.forEach(i -> i.setCompany(this));
        }
        this.privacyRequests = privacyRequests;
    }

    public Company privacyRequests(Set<PrivacyRequest> privacyRequests) {
        this.setPrivacyRequests(privacyRequests);
        return this;
    }

    public Company addPrivacyRequest(PrivacyRequest privacyRequest) {
        this.privacyRequests.add(privacyRequest);
        return this;
    }

    public Company removePrivacyRequest(PrivacyRequest privacyRequest) {
        this.privacyRequests.remove(privacyRequest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Company)) {
            return false;
        }
        return id != null && id.equals(((Company) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Company{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", taxIdNumber='" + getTaxIdNumber() + "'" +
            ", officeAddress='" + getOfficeAddress() + "'" +
            "}";
    }
}
