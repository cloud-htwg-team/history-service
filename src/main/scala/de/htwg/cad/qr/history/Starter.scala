package de.htwg.cad.qr.history

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives.{pathEnd, _}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import de.htwg.cad.qr.history.db.HistoryPersistenceHandler
import spray.json.DefaultJsonProtocol._

import java.util.Base64
import scala.concurrent.ExecutionContextExecutor
import scala.util.Try

object Starter extends App with JsonParser {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val persistence: HistoryPersistenceHandler = HistoryPersistenceHandler.mock

  implicit def myExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case t: Throwable =>
        t.printStackTrace()
        complete(HttpResponse(InternalServerError))
    }

  val route = Route.seal(
    pathPrefix("history" / "tenants" / Segment) { tenantId => {
      concat(
        path("entries") {
          get {
            complete(persistence.getTenantEntries(tenantId))
          }
        },
        pathPrefix("users" / Segment / "entries") { userId => {
          concat(
            pathEnd {
              concat(
                post {
                  entity(as[CodeAdditionRequest]) { request =>
                    Try(Base64.getDecoder.decode(request.qrCode)) match {
                      case scala.util.Success(_) => complete(persistence.postEntry(tenantId, userId, request))
                      case scala.util.Failure(_) => complete(HttpResponse(BadRequest, entity = "could not decode base64 qr code"))
                    }
                  }
                },
                get {
                  complete(persistence.getUserEntries(tenantId, userId))
                }
              )
            },
            pathPrefix(Segment) { entryId =>
              concat(
                pathEnd {
                  get {
                    complete(persistence.getEntry(tenantId, userId, entryId))
                  }
                },
                path("code") {
                  get {
                    complete(persistence.getQrCode(tenantId, userId, entryId))
                  }
                }
              )
            })
        }
        }
      )
    }})

  val bindingFuture = Http().newServerAt("0.0.0.0", 8888).bind(route)
}
