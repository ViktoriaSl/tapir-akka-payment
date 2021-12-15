package dependencies

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import com.softwaremill.macwire.wire
import com.typesafe.config.{Config, ConfigFactory}
import payments.application.PaymentsService
import payments.config.{ApiConf, PaymentConf, ServerConf, ServiceConf}
import pureconfig.ConfigReader.Result
import pureconfig._
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext

trait CommonLayer {
  self =>
  implicit val system: ActorSystem

  implicit def executor: ExecutionContext

  lazy val serviceConf: ServiceConf = ConfigSource.default.load[ServiceConf] match {
    case Right(conf) => conf
    case Left(error) =>
      throw new Exception(error.toString())
  }
  lazy val serverConf: ServerConf = serviceConf.http

  lazy val apiConf: ApiConf = serviceConf.api

  lazy val config: Config = ConfigFactory.load()

  implicit lazy val paymentConf: PaymentConf = serviceConf.api.payment
  implicit lazy val logger: LoggingAdapter = system.log

}
