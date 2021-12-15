package payments.application

import akka.Done
import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.Materializer
import payments.config.ApiConf
import payments.db.PaymentRepository
import payments.model.{ErrorInfo, Payment, PaymentRequest, PaymentsResponse, StatResponse}

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

class PaymentsService(paymentRepository: PaymentRepository)(implicit ec: ExecutionContext, mat: Materializer, system: ActorSystem) {
  private val logger = Logging(system, getClass)

  def getCryptoCurrenciesList: Future[List[String]] = {

    paymentRepository.getCryptoCurrenciesList
  }

  def getFiatCurrenciesList: Future[List[String]] = {

    paymentRepository.getFiatCurrenciesList
  }

  def createNewPayment(request: PaymentRequest): Future[Done] = {

    logger.info(s"Insert payment: ${request.fiatCurrency}")

    val newPayment = Payment.from(request)

    paymentRepository.insertPayments(newPayment)
  }

  def getPaymentById(id: UUID): Future[Option[PaymentsResponse]] = {
    logger.info(s"Get request for payment with id: $id")
    import Payment.to

    for {
      payment <- paymentRepository.getPayment(id)
    } yield {
     payment.map(to)
    }
  }

  def getPayments(fiatCurrency: String): Future[List[PaymentsResponse]] = {

    logger.info(s"Getting payment with fiatCurrency : $fiatCurrency")
    import Payment.to
    for{
      payments <- paymentRepository.getPaymentsList(fiatCurrency)
    } yield{
      payments.map(to)
    }
  }

  def getPaymentsStatistic(fiatCurrency: String): Future[StatResponse] = {

    logger.info(s"Getting statistic with fiatCurrency : $fiatCurrency")

    for{
      payments <- paymentRepository.getPaymentsList(fiatCurrency)
      paymentSize = payments.size.toString
      paymentCurrencyAmount = payments.groupBy(_.coinCurrency).size.toString
      paymentCoinTotalAmount = payments.map(_.coinAmount).sum
    } yield{
      StatResponse(paymentSize, paymentCurrencyAmount, paymentCoinTotalAmount)
    }
  }
}
