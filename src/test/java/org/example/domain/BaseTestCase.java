package org.example.domain;

import org.example.bootstrap.BootstrapEbean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.ebean.EbeanServer;

public class BaseTestCase {

  private static final Logger log = LoggerFactory.getLogger(BaseTestCase.class);

  static {
    BootstrapEbean bootstrapEbean = new BootstrapEbean();
    EbeanServer ebeanServer = bootstrapEbean.create();

    log.info("Bootstrap EbeanServer instance {}", ebeanServer.getName());
  }
}
