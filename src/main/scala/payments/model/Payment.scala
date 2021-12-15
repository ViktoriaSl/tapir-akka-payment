package payments.model

import java.time.LocalDateTime
import java.util.UUID

case class Payment(id: UUID,
                   fiatAmount: BigDecimal,
                   fiatCurrency: String,
                   coinAmount: BigDecimal,
                   coinCurrency: String,
                   exchangeRate: BigDecimal,
                   eurExchangeRate: BigDecimal,
                   createdAt: LocalDateTime,
                   expirationTime: LocalDateTime)
object Payment{
  // coinAmount (asked crypto amount)
  val coinAmount: BigDecimal = 0

  // exchangeRate  (fiat to euro)
  val eurExchangeRate : BigDecimal = MarketData.exchangeRatesToEUR("EUR")

  // exchangeRate  (fiat to cripto)
  val exchangeRate : BigDecimal = MarketData.exchangeRatesToEUR("BTC")

  def from(paymentRequest: PaymentRequest): Payment = {
    new Payment(
      id = UUID.randomUUID(),
      fiatAmount = paymentRequest.fiatAmount,
      fiatCurrency = paymentRequest.fiatCurrency,
      coinCurrency = paymentRequest.coinCurrency,
      coinAmount = coinAmount,
      exchangeRate =  exchangeRate,
      eurExchangeRate = eurExchangeRate,
      createdAt = LocalDateTime.now(),
      expirationTime = LocalDateTime.now()
    )
  }

  def to(payment: Payment): PaymentsResponse = {
    PaymentsResponse(
      id = payment.id,
      fiatAmount = payment.fiatAmount,
      fiatCurrency = payment.fiatCurrency,
      coinAmount = payment.coinAmount,
      coinCurrency = payment.coinCurrency,
      exchangeRate = payment.exchangeRate,
      createdAt = payment.createdAt,
      expirationTime = payment.expirationTime
    )
  }
}
