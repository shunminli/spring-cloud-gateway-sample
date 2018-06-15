/**
 * @(#)TestController.java, Jun 15, 2018.
 *
 * Copyright 2018 lillard. All rights reserved. Use is subject to license terms.
 */
package com.example.testserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Lillard
 */
@RestController
@RequestMapping("${cluster}/test/")
public class TestController extends BaseController {

  @GetMapping("/clusters")
  public Mono<String> getCluster() {
    return Mono.just(this.cluster);
  }
}
