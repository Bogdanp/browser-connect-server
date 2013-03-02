package model

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import play.api.libs.concurrent._
import play.api.libs.iteratee._
import play.api.libs.json._
import play.api.Play.current

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import scala.language.postfixOps

object Coordinator {
  implicit val timeout = Timeout(5 seconds)

  lazy val default = Akka.system.actorOf(Props[Coordinator])

  def listen: Future[(Iteratee[JsValue, _], Enumerator[JsValue])] =
    (default ? Listen).map {
      case Connect(out) =>
        (Iteratee.foreach[JsValue] {_ =>}, out)
    }
}

case class Coordinator() extends Actor {
  val (enumerator, channel) = Concurrent.broadcast[JsValue]

  def receive = {
    case Listen =>
      sender ! Connect(enumerator)
    case Send(message) => 
      channel.push(message)
  }
}

sealed trait Action
case class Listen() extends Action
case class Connect(channel: Enumerator[JsValue]) extends Action
case class Send(message: JsValue) extends Action
