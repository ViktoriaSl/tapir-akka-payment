package dependencies

import com.softwaremill.macwire.wire
import payments.db.PaymentRepository

trait DatabaseComponents { self: CommonLayer =>
  lazy val paymentRepository: PaymentRepository         = wire[PaymentRepository]
}
