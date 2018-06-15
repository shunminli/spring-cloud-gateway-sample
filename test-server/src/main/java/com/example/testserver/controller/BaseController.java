/**
 * @(#)BaseController.java, Jun 15, 2018.
 *
 * Copyright 2018 lillard. All rights reserved. Use is subject to license terms.
 */
package com.example.testserver.controller;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author Lillard
 */
public class BaseController {

  @Value("${cluster}")
  protected String cluster;
}
