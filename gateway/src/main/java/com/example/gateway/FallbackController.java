/**
 * @(#)FallbackController.java, Jun 25, 2018.
 *
 * Copyright 2018 lillard. All rights reserved. Use is subject to license terms.
 */
package com.example.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author Lillard
 */
@RestController
public class FallbackController {

  @GetMapping("/fallback")
  public Mono<String> fallback() {
    return Mono.just("fallback");
  }
}
