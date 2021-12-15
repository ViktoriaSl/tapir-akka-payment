package payments.validation

sealed trait CurrencyValidation {
  def errorMessage: String
}

case object UnsupportedFiatCurrencyType extends CurrencyValidation {
  def errorMessage: String = "Fiat type of the currency does not supported."
}

case object UnsupportedCryptoCurrencyType extends CurrencyValidation {
  def errorMessage: String = "Crypto type of the currency does not supported."
}

case object UnsupportedCurrencyRange extends CurrencyValidation {
  def errorMessage: String = "Range of the currency does not supported."
}
