package payments.db

import akka.Done
import payments.model.Payment

import java.util.UUID
import scala.concurrent.Future

class PaymentRepository {

  def insertPayments(payment: Payment): Future[Done] = {

    // mock for storage
    DB.payments = DB.payments.+:(payment)

    Future.successful(Done)
  }

  def getPayment(id: UUID): Future[Option[Payment]] = {

    // mock for storage
    Future.successful(DB.payments.find(_.id == id))
  }

  def getPaymentsList(fiatCurrency: String): Future[List[Payment]] = {

    // mock for storage
    Future.successful(DB.payments.filter(_.fiatCurrency == fiatCurrency))
  }

  def getFiatCurrenciesList: Future[List[String]] = {

    // mock for storage
    Future.successful(DB.fiatCurrencies)
  }

  def getCryptoCurrenciesList: Future[List[String]] = {

    // mock for storage
    Future.successful(DB.cryptoCurrencies)
  }
}
