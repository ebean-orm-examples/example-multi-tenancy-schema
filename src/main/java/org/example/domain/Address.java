package org.example.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Address extends BaseDomain {

	public String description;

	@ManyToMany(mappedBy = "addresses")
	public List<Author> authors;

	public Address() {
	}

	public Address(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	@Override
	public String toString() {
		return "Address [description=" + description + "]";
	}

}
