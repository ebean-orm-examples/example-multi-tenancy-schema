package org.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import io.ebean.annotation.EnumValue;

@Entity
public class Role extends BaseDomain {

	public static enum RoleName {

		@EnumValue("admin") ADMIN("admin");

		private String value;

		private RoleName(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

	@Column(name = "name")
	public RoleName name;

	public RoleName getName() {
		return name;
	}

	public void setName(RoleName name) {
		this.name = name;
	}

}
