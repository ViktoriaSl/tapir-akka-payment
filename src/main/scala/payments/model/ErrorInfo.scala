package payments.model

import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import sttp.tapir.Schema

case class ErrorInfo(error: String)

object ErrorInfoHelper{
  implicit lazy val ErrorInfoSchema: Schema[ErrorInfo] = Schema.derived
  implicit lazy val ErrorInfoCodec: Codec[ErrorInfo]   = deriveCodec
}
