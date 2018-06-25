package com.example

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class GatewaySimulation extends Simulation {

  val httpConf = http.baseURL("http://localhost:8081/")

  val nodelay = scenario("gateway simulation nodelay").repeat(10) {
    exec(http("nodelay").get("lb/customers/nodelay").check(status.is(200)))
  }

  val delay1 = scenario("gateway simulation delay1").repeat(10) {
    exec(http("delay1").get("lb/customers/delay1").check(status.is(200)))
  }

  val delay1withHystrix = scenario("gateway simulation delay1 & hystrix").repeat(10) {
    exec(http("delay1 with hystrix").get("cb/customers/delay1").check(status.is(200)))
  }

  val delay2 = scenario("gateway simulation delay2").repeat(10) {
    exec(http("delay2").get("lb/customers/delay2").check(status.is(200)))
  }

  val delay2withHystrix = scenario("gateway simulation delay2 & hystrix").repeat(10) {
    exec(http("delay2 with hystrix").get("cb/customers/delay2").check(status.is(200)))
  }

  setUp(
    nodelay.inject(rampUsers(100) over (1)),
    delay1.inject(rampUsers(100) over (1)),
    delay1withHystrix.inject(rampUsers(100) over (1)),
    delay2.inject(rampUsers(100) over (1)),
    delay2withHystrix.inject(rampUsers(100) over (1))
  ).protocols(httpConf)
}