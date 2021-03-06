package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.clap.model.enumeration.UserType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
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
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "favourites", "notifications", "projects","followed","followers" })
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @Property("user_id")
    private String id;

    @NotNull
	String type;

    @Size(max = 20, message = "maximux size 20")
    @Property("username")
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
    @Property("createdDate")
    @JsonIgnore
    private Date createdDate;

    @Property("phone")
    @Column(nullable = true)
    private String phone;

    @Size(max = 256)
    @Property("photoUrl")
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
}
