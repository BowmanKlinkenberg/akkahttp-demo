package com.phatlabs.ninja

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives
import com.phatlabs.ninja.repository.TodoRepository

trait Router {
  def route: Route
}

class TodoRouter(todoRepository: TodoRepository) extends Router with Directives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  override def route: Route = pathPrefix("todos") {
    pathEndOrSingleSlash {
      get {
        complete(todoRepository.all())
      }
    }
  }
}
