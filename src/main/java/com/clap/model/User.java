package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

import com.clap.model.enumeration.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

/**
 * A user.
 */
@Node
@Getter
@Setter
public class User {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @Property("user_id")
    private String id;

    @NotNull
	UserType type;

    @Size(max = 20)
    @Property("user_name")
    @Column(nullable = false, unique=true)
    private String username;

    @Email
    @Size(min = 5, max = 254)
    @Column(nullable = false, unique=true)
    private String email;

    @JsonIgnore
    @NotNull
    @Size(min = 8, max = 40,message = "The password must has between 8 and 40 characters")
    @Column(nullable = false)
    private String password;

    @CreatedDate
    @Property("created_date")
    @JsonIgnore
    private Instant createdDate = Instant.now();

    @Property("phone")
    @Column(nullable = true)
    private String phone;

    @Size(max = 256)
    @Property("photo_url")
    @Column(nullable = true)
    private String photoUrl;


    @Relationship("HAS_ARTISTIC_CONTENT")
    @JsonIgnoreProperties(value = { "tags", "projects", "owner", "users_favourites" }, allowSetters = true)
    private Set<ArtisticContent> favourites = new HashSet<>();

    @Relationship("HAS_NOTIFICATION")
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<Notification> notifications = new HashSet<>();

    @Relationship("HAS_PROJECT")
    @JsonIgnoreProperties(value = { "artisticContents", "user" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    @Relationship("HAS_USER")
    @JsonIgnoreProperties(value = { "favourites","notifications","projects","followed","followers" }, allowSetters = true)
    private Set<User> followed = new HashSet<>();

    @Relationship("HAS_USER")
    @JsonIgnoreProperties(value = { "favourites","notifications","projects","followed","followers" }, allowSetters = true)
    private Set<User> followers = new HashSet<>();

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "User{" +
            "username='" + getUsername() + '\'' +
            ", email='" + getEmail() + '\'' +
            ", phone='" + getPhone() + '\'' +
            ", photoUrl='" + getPhotoUrl() + '\'' +
            "}";
    }
}
