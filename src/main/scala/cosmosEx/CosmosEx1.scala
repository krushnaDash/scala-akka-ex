package com.krushna
package cosmosEx

import com.azure.cosmos.CosmosClientBuilder
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object CosmosEx1 extends App {

  def toSha256(message: String): String =
    String.format("%064x", new java.math.BigInteger(1,
      java.security.MessageDigest.getInstance("SHA-256").digest(message.getBytes("UTF-8"))))


  val objectMapper = new ObjectMapper()

  objectMapper.registerModule(DefaultScalaModule)
  objectMapper.registerModule(new JavaTimeModule)
  objectMapper.setSerializationInclusion(Include.NON_EMPTY)
  objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

  val cosmosClient = new CosmosClientBuilder()
    .endpoint("https://bua-cosmos.documents.azure.com:443/")
    .key("kMfNDCbyJuUESWU95cDsKgSDoAMre0F6UifOWQW36cndWm6hug6o7eNQcJ0rVMIcCv4ArFFgXQq5ACDb7EJouQ==")
    .buildClient();

  val cosmosContainer = cosmosClient.getDatabase("buabookkeeping").getContainer("nrtcategorysales")
  val businessUnit=BusinessUnit("1001","1","US","US")
  val categoryIdentifiers=CategoryIdentifiers(None, Option("1") ,None,Option("23"))
  val categoryKey = CategorySalesKey("c1", businessUnit, "2023-04-04", Option(categoryIdentifiers))
  val shaId = toSha256(categoryKey.toString)

  val nrtcategoriesSales= NrtCategorySale(shaId,categoryKey,122.40,List(OrderIdentifier("1",None,None),OrderIdentifier("2",None,None)),None)

  println("josn value >>>> "+ objectMapper.writeValueAsString(nrtcategoriesSales))
    cosmosContainer.createItem(objectMapper.valueToTree(nrtcategoriesSales))
  cosmosClient.close()

}



