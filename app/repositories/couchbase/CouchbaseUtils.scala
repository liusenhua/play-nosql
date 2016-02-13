package repositories.couchbase

import com.couchbase.client.protocol.views.{Stale, Query}

import play.api.libs.json.Json

/**
 * Created by andy on 2/10/16.
 */
object CouchbaseUtils {
  def createQuery(keys: Option[List[String]] = None, stale: Boolean): Query = {
    val query = new Query()
    query.setIncludeDocs(true)
    query.setStale(Stale.FALSE)
    if (keys.isDefined) {
      query.setKeys(Json.stringify(Json.toJson(keys.get)))
    }
    query
  }

  def createQuery(key: String, stale: Boolean): Query = {
    val query = new Query()
    query.setIncludeDocs(true)
    query.setStale(Stale.FALSE)
    query.setKey(Json.stringify(Json.toJson(key)))
  }

}
