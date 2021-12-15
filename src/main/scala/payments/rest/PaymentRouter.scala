package payments.rest

import akka.Done
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import cats.data.NonEmptyChain
import org.slf4j.LoggerFactory
import payments.application.PaymentsService
import payments.model.{ErrorInfo, PaymentRequest, PaymentsResponse, StatResponse}
import sttp.tapir.Schema._
import sttp.tapir.codec.newtype._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.Schema._
import sttp.tapir.server.ServerEndpoint.Full
import sttp.tapir._
import sttp.tapir.generic.auto._
import payments.model.PaymentsResponseHelper._
import payments.model.PaymentRequestHelper._
import payments.config.PaymentConf
import sttp.tapir.json.circe._
import io.circe.generic.auto._
import payments.validation.{PaymentRequestFormValidatorNec, UnsupportedCryptoCurrencyType, UnsupportedFiatCurrencyType}

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success, Try}
import sttp.tapir.Validator._
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

class PaymentRouter(paymentsService: PaymentsService)(implicit ex: ExecutionContext, mat: Materializer, payment: PaymentConf) {

  import PaymentEndpoints._

  private val logger = LoggerFactory.getLogger(getClass)

  private def pagingValidator: Validator[PaymentRequest] = Validator.all[PaymentRequest](
    max[BigDecimal](payment.maxEurAmount).and(min[BigDecimal](payment.minEurAmount)).contramap(_.fiatAmount),
    nonEmptyString[String].contramap(_.fiatCurrency),
    nonEmptyString[String].contramap(_.coinCurrency)
  )

  val newPayment: Full[Unit, Unit, PaymentRequest, ErrorInfo, Done, scala.Any, Future] = baseEndPoint.in("new")
    .in(jsonBody[PaymentRequest].validate(pagingValidator))
    .out(jsonBody[Done])
    .serverLogicSuccess { request =>
      val cryptoList = paymentsService.getCryptoCurrenciesList
      val fiatCurrencyList = paymentsService.getFiatCurrenciesList

      val validatePossibleValues = for {
        fiatList <- fiatCurrencyList
        cryptoList <- cryptoList
      } yield {

        PaymentRequestFormValidatorNec.validateForm(
          fiatCurrency = request.fiatCurrency,
          cryptoCurrency = request.coinCurrency,
          fiatAmount = request.fiatAmount,
          possibleFiatTypes = fiatList,
          possibleCryptoTypes = cryptoList
        ).toEither

      }

      validatePossibleValues.flatMap {
        case Left(_) =>
          Future.successful(throw new Exception(s"Invalid data input, possible values ${request.fiatCurrency}"))
        case Left(_) =>
          Future.successful(throw new Exception(s"Invalid data input, possible values ${request.coinCurrency}"))
        case Right(r) => paymentsService.createNewPayment(r)
      }.andThen(handleErrors(_))
    }

  val getPaymentById: Full[Unit, Unit, UUID, ErrorInfo, PaymentsResponse, scala.Any, Future] =
    baseEndPoint.get
      .in(path[UUID]("id"))
      .out(jsonBody[PaymentsResponse]).serverLogicSuccess { path =>
      paymentsService.getPaymentById(path)
        .flatMap {
          case Some(data) => Future.successful(data)
          case None => Future.successful(throw new Exception("Resource not found."))
        }.andThen(handleErrors(_))
    }

  val getPaymentList: Full[Unit, Unit, String, ErrorInfo, List[PaymentsResponse], scala.Any, Future] =
    baseEndPoint.get
      .in(query[String]("currency"))
      .out(jsonBody[List[PaymentsResponse]]).serverLogicSuccess { path => paymentsService.getPayments(path).andThen(handleErrors(_)) }

  val getPaymentStat: Full[Unit, Unit, String, ErrorInfo, StatResponse, scala.Any, Future] =
    baseEndPoint.get
      .in("stats")
      .in(query[String]("currency"))
      .out(jsonBody[StatResponse]).serverLogicSuccess { path => paymentsService.getPaymentsStatistic(path).andThen(handleErrors(_)) }

  val routes: Route = AkkaHttpServerInterpreter().toRoute(List(newPayment, getPaymentById, getPaymentList, getPaymentStat))

  private def handleErrors[T](t: Try[T]): Either[ErrorInfo, T] = {
    t match {
      case Failure(e) =>
        logger.error("Exception when running endpoint logic", e)
        Left(ErrorInfo(e.getMessage))
      case Success(v) => Right(v)
    }
  }
}
