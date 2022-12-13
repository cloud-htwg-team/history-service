package de.htwg.cad.timetracker.tracking

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContextExecutor

object Starter extends App with JsonParser {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val dataBase = new MockDatabase

  val route = {
    pathPrefix("history" / Segment / "elements") { tenantId => {
          concat(
            pathEnd {
              concat(
                post {
                  entity(as[TimeTrackingElement]) { request =>
                    complete(dataBase.postEntry(tenantId, request))
                  }
                },
                get {
                  complete(dataBase.getAllEntries(tenantId))
                }
              )
            },
            pathPrefix(Segment) { entryId =>
              concat(
                get {
                  complete(dataBase.getEntry(tenantId, entryId))
                }
              )
            })
    }}}

  val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bind(route)
}

final case class TimeTrackingElement(createdAt: Long, codeUrl: String, createdBy: String)
