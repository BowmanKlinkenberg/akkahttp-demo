package com.phatlabs.ninja.model

import akka.http.scaladsl.model.{StatusCode, StatusCodes}

final case class ApiError private (statusCode: StatusCode, message: String)

object ApiError {
  def todoNotFound(id: String): ApiError =
    new ApiError(StatusCodes.NotFound,
                 "sThe todo with id $id could not be found.")

  private def apply(statusCode: StatusCode, message: String): ApiError =
    new ApiError(statusCode, message)

  val generic: ApiError =
    new ApiError(StatusCodes.InternalServerError, "Unknown error.")

  val emptyTitleField: ApiError =
    new ApiError(StatusCodes.BadRequest, "The title field must not be empty")

}
