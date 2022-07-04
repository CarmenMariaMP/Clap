package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * A Company.
 */
@Node
@Getter
@Setter
public class Company extends User {

    @Property("company_name")
    private String companyName;

    @Property("tax_id_number")
    private String taxIdNumber;

    @Property("office_address")
    private String officeAddress;

    @Relationship("HAS_PRIVACY_REQUEST")
    @JsonIgnoreProperties(value = { "company", "contentCreatto" }, allowSetters = true)
    private Set<PrivacyRequest> privacyRequests = new HashSet<>();


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

    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
        result = prime * result + ((officeAddress == null) ? 0 : officeAddress.hashCode());
        result = prime * result + ((taxIdNumber == null) ? 0 : taxIdNumber.hashCode());
        return result;
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Company other = (Company) obj;
        if (companyName == null) {
            if (other.companyName != null)
                return false;
        } else if (!companyName.equals(other.companyName))
            return false;
        if (officeAddress == null) {
            if (other.officeAddress != null)
                return false;
        } else if (!officeAddress.equals(other.officeAddress))
            return false;
        if (taxIdNumber == null) {
            if (other.taxIdNumber != null)
                return false;
        } else if (!taxIdNumber.equals(other.taxIdNumber))
            return false;
        return true;
    }
}
