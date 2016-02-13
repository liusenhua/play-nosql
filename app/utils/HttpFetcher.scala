package utils

import play.api.libs.json.JsValue
import play.api.libs.ws.WS
import play.api.Play.current

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by andy on 2/8/16.
 */
trait HttpFetcher extends Fetcher {
  override def fetch(url: String, timeout: Int = 60000, headers: List[(String, String)] = List.empty) = {
    WS.url(url).withRequestTimeout(timeout).withHeaders(headers: _*).get() map { ret =>
      ret
    }
  }

  override def fetchPostJson(url: String, body: JsValue, timeout: Int = 60000, headers: List[(String, String)] = List.empty) = {
    WS.url(url).withRequestTimeout(timeout).withHeaders(headers: _*).post(body) map { ret =>
    ret
    }
  }

  override def fetchPutJson(url: String, body: JsValue, timeout: Int = 60000, headers: List[(String, String)] = List.empty) = {
    WS.url(url).withRequestTimeout(timeout).withHeaders(headers: _*).put(body) map { ret =>
      ret
    }
  }

  override def fetchDelete(url: String, timeout: Int = 60000, headers: List[(String, String)] = List.empty) = {
    WS.url(url).withRequestTimeout(timeout).withHeaders(headers: _*).delete() map { ret =>
      ret
    }
  }

}
