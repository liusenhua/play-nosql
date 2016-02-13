import play.Logger
import play.api.libs.json.{JsResultException, JsValue}
import play.api.mvc.{Results, Result, AnyContent, Request}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by andy on 2/10/16.
 */
package object controllers {
  def parseRequestJson(request: Request[AnyContent], onAfterParse: JsValue => Future[Result]): Future[Result] = {
    try {
      val json = request.body.asJson
      json.map {
        json => onAfterParse(json)
      }.getOrElse {
        Logger.error("Bad json:" + request.body.asText.getOrElse("No data in body"))
        Future(Results.BadRequest("No json data"))
      }
    } catch {
      case e: JsResultException =>
        Logger.error("Unable to parse json:" + request.body.asText.getOrElse("No data in body") + " - " + e.getMessage)
        Future(Results.BadRequest("Unable to parse json data - " + e.getMessage))
    }
  }
}
