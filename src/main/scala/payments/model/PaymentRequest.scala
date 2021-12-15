package payments.model

import io.circe.generic.semiauto.deriveCodec
import io.circe.Codec
import sttp.tapir.Schema

case class PaymentRequest(fiatAmount: BigDecimal, fiatCurrency: String, coinCurrency: String)

object PaymentRequestHelper{
  implicit lazy val PaymentRequestSchema: Schema[PaymentRequest] = Schema.derived
  implicit lazy val PaymentRequestCodec: Codec[PaymentRequest]   = deriveCodec
}