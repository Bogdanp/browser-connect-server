package controllers

import model._

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Json._

object Application extends Controller {
  def evaluateJS = Resource { request =>
    Coordinator.default ! Send(JsObject(Seq(
      "action" -> JsString("evaluateJS"),
      "source" -> JsString(request.body))))
  }

  def reloadCSS = Resource { request =>
    Coordinator.default ! Send(JsObject(Seq(
      "action" -> JsString("reloadCSS"))))
  }

  def socket = WebSocket.async[JsValue](_ => Coordinator.listen)

  protected def Resource(fn: Request[String] => Any) =
    Action(parse.tolerantText) {
      implicit request =>
        fn(request)

        Ok(toJson(JsObject(Seq(
          "success" -> JsBoolean(true)))))
    }
}
