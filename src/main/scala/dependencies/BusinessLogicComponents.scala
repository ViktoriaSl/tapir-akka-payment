package dependencies
import com.softwaremill.macwire.wire
import payments.application.PaymentsService

trait BusinessLogicComponents { self: CommonLayer with DatabaseComponents =>
  lazy val paymentsService: PaymentsService = wire[PaymentsService]
}
