package dependencies

import com.softwaremill.macwire.wire
import payments.rest.PaymentRouter

trait HttpComponents { self: CommonLayer with BusinessLogicComponents =>
  lazy val paymentRouter: PaymentRouter = wire[PaymentRouter]
}