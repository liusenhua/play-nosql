package utils

import play.api.libs.json.JsValue
import play.api.libs.ws.WSResponse

import scala.concurrent.Future

/**
 * Created by andy on 2/8/16.
 */
trait Fetcher {
  def fetch(url: String, timeout: Int = 10000, headers: List[(String, String)] = List.empty): Future[WSResponse]

  def fetchPostJson(url: String, body: JsValue, timeout: Int = 10000, headers: List[(String, String)] = List.empty): Future[WSResponse]

  def fetchPutJson(url: String, body: JsValue, timeout: Int = 10000, headers: List[(String, String)] = List.empty): Future[WSResponse]

  def fetchDelete(url: String, timeout: Int = 10000, headers: List[(String, String)] = List.empty): Future[WSResponse]
}
