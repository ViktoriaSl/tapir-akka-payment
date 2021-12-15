package payments.rest

import payments.model.ErrorInfo
import sttp.tapir.{Endpoint, endpoint}
import sttp.tapir._

object PaymentEndpoints {

  type PublicEndpoint[I, E, O, -R] = Endpoint[Unit, I, E, O, R]

  val baseEndPoint: PublicEndpoint[Unit, ErrorInfo, Unit, Any] = endpoint.in("payment").errorOut(stringBody.mapTo[ErrorInfo])
}
