/**
 * @(#)CustomerController.java, Jun 19, 2018.
 *
 * Copyright 2018 lillard. All rights reserved. Use is subject to license terms.
 */
package com.example.customerservice.controller;

import com.example.customerservice.data.Customer;
import com.example.customerservice.storage.CustomerStorage;
import java.time.Duration;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Lillard
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

  @Autowired
  private CustomerStorage customerStorage;

  @GetMapping
  public Flux<Customer> getAll() {
    return customerStorage.findAll();
  }

  @GetMapping("/{id}")
  public Mono<Customer> get(@PathVariable String id) {
    return customerStorage.findById(id);
  }

  @GetMapping("/delay1")
  public Flux<Customer> getAllDelay1() {
    return customerStorage.findAll().delayElements(Duration.ofMillis(1000));
  }

  @GetMapping("/delay2")
  public Flux<Customer> getAllDelay2() {
    return customerStorage.findAll().delayElements(Duration.ofMillis(RandomUtils.nextInt(600)));
  }
}
