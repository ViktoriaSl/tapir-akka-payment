package payments.model

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import sttp.tapir.Schema

import java.time.LocalDateTime
import java.util.UUID

case class PaymentsResponse(id: UUID,
                            fiatAmount: BigDecimal,
                            fiatCurrency: String,
                            coinAmount: BigDecimal,
                            coinCurrency: String,
                            exchangeRate: BigDecimal,
                            createdAt: LocalDateTime,
                            expirationTime: LocalDateTime)

object PaymentsResponseHelper {
  implicit lazy val PaymentsResponseSchema: Schema[PaymentsResponse] = Schema.derived
  implicit lazy val PaymentsResponseCodec: Codec[PaymentsResponse] = deriveCodec
}
