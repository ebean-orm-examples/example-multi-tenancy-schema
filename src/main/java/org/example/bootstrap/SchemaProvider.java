package org.example.bootstrap;

import com.avaje.ebean.config.TenantSchemaProvider;

class SchemaProvider implements TenantSchemaProvider {

  @Override
  public String schema(String tenantId) {
    return (tenantId == null) ? "public" : tenantId;
  }

}
