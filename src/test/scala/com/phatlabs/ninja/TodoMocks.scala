package com.phatlabs.ninja

import com.phatlabs.ninja.model.{CreateTodo, Todo, UpdateTodo}
import com.phatlabs.ninja.repository.TodoRepository

import scala.concurrent.Future

trait TodoMocks {
  class FailingRepository extends TodoRepository {
    override def all(): Future[Seq[Todo]] =
      Future.failed(new Exception("Mock failure: all()"))

    override def done(): Future[Seq[Todo]] =
      Future.failed(new Exception("Mock failure: done()"))

    override def pending(): Future[Seq[Todo]] =
      Future.failed(new Exception("Mock failure: pending()"))

    override def save(createTodo: CreateTodo): Future[Todo] =
      Future.failed(new Exception("Mock failure: create()"))

    override def update(id: String, updateTodo: UpdateTodo): Future[Todo] =
      Future.failed(new Exception("Mock failure: update()"))
  }

}
