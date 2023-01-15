package de.htwg.cad.qr.history

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives.{pathEnd, _}
import de.htwg.cad.qr.history.db.HistoryPersistenceHandler
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContextExecutor

object Starter extends App with JsonParser {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val persistence: HistoryPersistenceHandler = HistoryPersistenceHandler.mock

  val route = {
    pathPrefix("history" / "tenant" / Segment) { tenantId => {
      concat(
        get {
          complete(persistence.getTenantEntries(tenantId))
        },
        pathPrefix("user" / Segment) { userId => {
          concat(
            pathEnd {
              concat(
                post {
                  entity(as[CodeAdditionRequest]) { request =>
                    complete(persistence.postEntry(tenantId, userId, request))
                  }
                },
                get {
                  complete(persistence.getUserEntries(tenantId, userId))
                }
              )
            },
            pathPrefix("entry" / Segment) { entryId =>
              concat(
                pathEnd {
                  get {
                    complete(persistence.getEntry(tenantId, userId, entryId))
                  }
                },
                pathSuffix("code") {
                  get {
                    complete(persistence.getQrCode(tenantId, userId, entryId))
                  }
                }
              )
            })
        }
        }
      )
    }}}

  val bindingFuture = Http().newServerAt("0.0.0.0", 8888).bind(route)
}
