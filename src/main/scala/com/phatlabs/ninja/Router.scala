package com.phatlabs.ninja

import scala.util.{Failure, Success}
import akka.http.scaladsl.server.{Directives, Route}
import com.phatlabs.ninja.directive.{
  CustomHeaderDirectives,
  TodoDirectives,
  ValidatorDirectives
}
import com.phatlabs.ninja.model.{ApiError, CreateTodo, UpdateTodo}
import com.phatlabs.ninja.repository.TodoRepository
import com.phatlabs.ninja.validation.{CreateTodoValidator, UpdateTodoValidator}

trait Router {
  def route: Route
}

class TodoRouter(todoRepository: TodoRepository)
    extends Router
    with Directives
    with TodoDirectives
    with ValidatorDirectives
    with CustomHeaderDirectives {
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._
  override def route: Route =
    pathPrefix("todos") {
      pathEndOrSingleSlash {
        get {
          headerValueByName("test-header") { test =>
            CustomAuthorizationDirective(test) { _ =>
              handleWithGeneric(todoRepository.all()) { todos =>
                complete(todos)
              }
            }
          } ~ post {
            entity(as[CreateTodo]) { createTodo =>
              validateWith(CreateTodoValidator)(createTodo) {
                handleWithGeneric(todoRepository.save(createTodo)) { todos =>
                  complete(todos)
                }
              }
            }
          }
        }
      }
    } ~ path(Segment) { id: String =>
      put {
        entity(as[UpdateTodo]) { updateTodo =>
          validateWith(UpdateTodoValidator)(updateTodo) {
            handle(todoRepository.update(id, updateTodo)) {
              case TodoRepository.TodoNotFound(_) =>
                ApiError.todoNotFound(id)
              case _ =>
                ApiError.generic
            } { todo =>
              complete(todo)
            }
          }
        }
      }
    } ~ path("done") {
      get {
        handleWithGeneric(todoRepository.done()) { todos =>
          complete(todos)
        }
      }
    } ~ path("pending") {
      get {
        handleWithGeneric(todoRepository.pending()) { todos =>
          complete(todos)
        }
      }
    }
}
