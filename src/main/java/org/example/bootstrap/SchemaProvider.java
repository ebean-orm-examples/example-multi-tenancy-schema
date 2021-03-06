package org.example.bootstrap;

import com.avaje.ebean.config.TenantSchemaProvider;

class SchemaProvider implements TenantSchemaProvider {

  @Override
  public String schema(Object tenantId) {
    if (tenantId == null) {
      throw new RuntimeException("No current tenantId supplied");
    }
    return tenantId.toString();
  }

}
