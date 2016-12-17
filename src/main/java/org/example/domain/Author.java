package org.example.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.avaje.ebean.annotation.Cache;

@Entity
@Cache(enableQueryCache = true)
public class Author extends BaseDomain {

	public String name;

	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	public List<Content> contents;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Address> addresses;

	@ManyToMany(cascade = CascadeType.ALL)
	public List<Role> roles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "Author [name=" + name + ", addresses=" + addresses + "]";
	}

}
