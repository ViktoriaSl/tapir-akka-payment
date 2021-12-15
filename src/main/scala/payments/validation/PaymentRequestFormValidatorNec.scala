package payments.validation

import cats.data.ValidatedNec
import cats.implicits.{catsSyntaxTuple3Semigroupal, catsSyntaxValidatedIdBinCompat0}
import payments.model.PaymentRequest

sealed trait PaymentRequestFormValidatorNec {
  type ValidationResult[A] = ValidatedNec[CurrencyValidation, A]

  def validateFiatCurrency(fiatCurrency: String, possibleTypes: List[String]): ValidationResult[String] =
    if (possibleTypes.contains(fiatCurrency)) fiatCurrency.validNec else UnsupportedFiatCurrencyType.invalidNec

  def validateCryptoCurrency(cryptoCurrency: String, possibleTypes: List[String]): ValidationResult[String] =
    if (possibleTypes.contains(cryptoCurrency)) cryptoCurrency.validNec else UnsupportedCryptoCurrencyType.invalidNec

  def validateCurrencyRange(fiatAmount: BigDecimal, minEurAmount: Int, maxEurAmount: Int): ValidationResult[BigDecimal] =
    if (fiatAmount > maxEurAmount && fiatAmount < minEurAmount) fiatAmount.validNec else UnsupportedCurrencyRange.invalidNec

  def validateForm(fiatCurrency: String, cryptoCurrency: String, fiatAmount: BigDecimal, possibleFiatTypes: List[String], possibleCryptoTypes: List[String]): ValidationResult[PaymentRequest] = {
    (fiatAmount.validNec,
      validateFiatCurrency(fiatCurrency, possibleFiatTypes),
      validateCryptoCurrency(cryptoCurrency, possibleCryptoTypes)
      ).mapN(PaymentRequest)
  }
}

object PaymentRequestFormValidatorNec extends PaymentRequestFormValidatorNec