/**
 * @(#)Customer.java, Jun 19, 2018.
 *
 * Copyright 2018 lillard. All rights reserved. Use is subject to license terms.
 */
package com.example.customerservice.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Lillard
 */
@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

  @Id
  private String id;
  private String name;
}
