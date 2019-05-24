package com.phatlabs.ninja.directive

import akka.http.scaladsl.server.AuthorizationFailedRejection
import akka.http.scaladsl.server.{Directive, Directive1, Directives}

trait CustomHeaderDirectives extends Directives {

  def CustomAuthorizationDirective(headers: String): Directive1[Boolean] = {
    val splitGroups = headers.split('|')

    try {
      val groups: List[String] = splitGroups.map { ldapName =>
        ldapName.split(',')(0).split("=")(1)
      }.toList

      groups.contains("APP-testing-test") match {
        case true  => provide(true)
        case false => reject(AuthorizationFailedRejection)
      }
    } catch {
      case _: java.lang.ArrayIndexOutOfBoundsException =>
        println("Malformed header...")
        reject(AuthorizationFailedRejection)
      case unknown =>
        println(unknown)
        reject(AuthorizationFailedRejection)
    }
  }

}
