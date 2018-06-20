/**
 * @(#)CustomerStorage.java, Jun 19, 2018.
 *
 * Copyright 2018 lillard. All rights reserved. Use is subject to license terms.
 */
package com.example.customerservice.storage;

import com.example.customerservice.data.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author Lillard
 */
public interface CustomerStorage extends ReactiveMongoRepository<Customer, String> {
}
