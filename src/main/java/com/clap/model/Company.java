package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * A Company.
 */
@Node
@Entity
@Data
@EqualsAndHashCode(callSuper = true, of = {})
@ToString(exclude = "privacyRequests")
@AllArgsConstructor
public class Company extends User {

    @Property("companyName")
    @NotNull
	@Column(unique = true, nullable = false)
    private String companyName;

    @NotNull
	@Column(unique = true, nullable = false)
    @Property("taxIdNumber")
    private String taxIdNumber;

    @NotNull
	@Column(unique = true)
    @Property("officeAddress")
    private String officeAddress;

    @Relationship("HAS_PRIVACY_REQUEST")
    @JsonIgnoreProperties(value = { "company", "contentCreatto" }, allowSetters = true)
    private Set<PrivacyRequest> privacyRequests = new HashSet<>();

    public Company() {
		super();
		setType("COMPANY");
	}
}
