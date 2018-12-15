package com.phatlabs.ninja.validation

import com.phatlabs.ninja.model.{ApiError, CreateTodo}

object TodoValidator {
  def validate(createTodo: CreateTodo): Option[ApiError] = {
    if (createTodo.title.isEmpty) Some(ApiError.emptyTitleField)
    else None
  }

}
