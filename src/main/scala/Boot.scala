import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import dependencies.{BusinessLogicComponents, CommonLayer, DatabaseComponents, HttpComponents}

import scala.concurrent.ExecutionContext

trait Setup extends HttpComponents
  with BusinessLogicComponents
  with DatabaseComponents
  with CommonLayer {

  lazy val apiRoutes: Route =
    paymentRouter.routes
}

object Boot extends App with Setup {
  override implicit val system: ActorSystem = ActorSystem("payment-bitclear", config)
  override implicit val executor: ExecutionContext = system.dispatcher

  Http().newServerAt(serverConf.interface, serverConf.port).bindFlow(apiRoutes)

  println(s"Server online at http://localhost:8080/")


}
