package controllers

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import play.api._
import play.api.mvc._
import play.api.libs.concurrent._
import play.api.libs.iteratee._
import play.api.libs.json._
import play.api.libs.json.Json._
import play.api.Play.current

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._

sealed trait Action
case class Listen() extends Action
case class Connect(channel: Enumerator[JsValue]) extends Action
case class Disconnect(channel: Enumerator[JsValue]) extends Action
case class Message(message: JsValue) extends Action

object Socket {
  implicit val timeout = Timeout(5 seconds)

  lazy val socket = Akka.system.actorOf(Props[Socket])

  def listen: Future[(Iteratee[JsValue, _], Enumerator[JsValue])] = {
    (socket ? Listen).map {
      case Connect(out) => {
        val in = Iteratee.foreach[JsValue] { _ => } mapDone { _ =>
          socket ! Disconnect(out)
        }

        (in, out)
      }
    }
  }
}

case class Socket() extends Actor {
  var listeners = Array[PushEnumerator[JsValue]]()

  def receive = {
    case Listen => {
      val channel = Enumerator.imperative[JsValue]()

      listeners ++= Seq(channel)

      sender ! Connect(channel)
    }
    case Disconnect(channel) =>
      listeners = listeners diff Seq(channel)
    case Message(message) => 
      for (channel <- listeners)
        channel.push(message)
  }
}

object Application extends Controller {
  def evaluateJS = Action(parse.tolerantText) { request =>
    Socket.socket ! Message(JsObject(Seq(
      "action" -> JsString("evaluateJS"),
      "source" -> JsString(request.body))))

    Ok(toJson(JsObject(Seq(
      "success" -> JsBoolean(true)))))
  }

  def reloadCSS = Action {
    Socket.socket ! Message(JsObject(Seq(
      "action" -> JsString("reloadCSS"))))

    Ok(toJson(JsObject(Seq(
      "success" -> JsBoolean(true)))))
  }

  def socket = WebSocket.async[JsValue](_ => Socket.listen)
}
