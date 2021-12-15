package payments.db

import payments.model.Payment

object DB {
  val fiatCurrencies: List[String] = List("EUR", "USD")
  val cryptoCurrencies: List[String] = List("BTC")
  var payments: List[Payment] = List.empty
}
