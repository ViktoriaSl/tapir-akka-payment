package payments.model

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import sttp.tapir.Schema

case class StatResponse(paymentsCount: String, paymentCurrencyAmount: String, paymentCoinTotalAmount: BigDecimal)

object StatResponse{
    implicit lazy val StatResponseSchema: Schema[StatResponse] = Schema.derived
    implicit lazy val StatResponseCodec: Codec[StatResponse]   = deriveCodec
}
