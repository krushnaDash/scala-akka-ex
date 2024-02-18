package com.krushna
package cosmosEx

case class CustomerOrderCategories(id: String,
                                   orderId: String,
                                   eventId: String,
                                   businessDate: String,
                                   businessUnit: BusinessUnit,
                                   categories: List[Category]
                                  )

case class BusinessUnit(id: String,
                        baseDivisionCode: String,
                        countryCode: String,
                        financialReportingGroup: String)

case class Category(
                     categoryName: String,
                     amount: Double,
                     orderIdentifiers: List[OrderIdentifier],
                     identifier: Option[CategoryIdentifiers],
                     supplementalDataList: Option[List[String]]
                   )

case class OrderIdentifier(orderId: String,
                           paymentIds: Option[List[String]],
                           itemIds: Option[List[String]])


case class CategoryIdentifiers(
                                accountingDivision: Option[String] = None,
                                salesDivision: Option[String] = None,
                                terminalId: Option[String] = None,
                                departmentNumber: Option[String] = None,
                                cftAccountNumber: Option[String] = None,
                                cftId: Option[Int] = None,
                                posSalesDepartmentNumber: Option[String] = None,
                                voucherType : Option[String] = None,
                                tenderSubType : Option[String] = None,
                                clientSubType : Option[String] = None,
                                operatorId : Option[String] = None)


case class CategorySale(id: String,
                        batchId: String,
                        key: CategorySalesKey,
                        amount: Double,
                        orderIdentifiers: List[OrderIdentifier],
                        supplementalDataList: Option[List[String]]
                       )

case class NrtCategorySale(id: String,
                        key: CategorySalesKey,
                        amount: Double,
                        orderIdentifiers: List[OrderIdentifier],
                        supplementalDataList: Option[List[String]]
                       )

case class CategorySalesKey(categoryName: String,
                            businessUnit: BusinessUnit,
                            businessDate: String,
                            identifier: Option[CategoryIdentifiers])

