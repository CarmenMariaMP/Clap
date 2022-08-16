package com.clap.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
/**
 * A user.
 */
@Node
@Data
@EqualsAndHashCode(of = "id")
@ToString(exclude = { "favourites"})
@NoArgsConstructor
public class User implements UserDetails{
    @Id
    @GeneratedValue(UUIDStringGenerator.class)
    @Property("user_id")
    private String id;

    @NotNull
	private String type;

    @Size(max = 20, message = "maximux size 20")
    @Size(min = 4, message = "minimum size 4")
    @Property("username")
    @Column(nullable = false, unique=true)
    private String username;

    @Email
    @Size(min = 5, max = 254)
    @Column(nullable = false, unique=true)
    private String email;

    @JsonIgnore
    @NotNull(message = "Not null")
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

    @Relationship("HAS_ARTISTIC_CONTENT_FAVOURITE")
    @JsonIgnoreProperties(value = { "tags", "owner", "users_favourites" }, allowSetters = true)
    private Set<ArtisticContent> favourites = new HashSet<>();

    @Relationship("HAS_ROLES")
    private List<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(this.type.toString()));
		return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
