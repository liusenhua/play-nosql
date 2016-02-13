package repositories.couchbase

import com.couchbase.client.protocol.views.{Stale, Query}

import net.spy.memcached.PersistTo
import org.reactivecouchbase.client.OpResult
import org.reactivecouchbase.play.PlayCouchbase

import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{Writes, Reads}
import play.api.{Logger, Play}

import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration

/**
 * Created by andy on 2/10/16.
 */
trait BaseCouchbaseRepository {
  val bucket = PlayCouchbase.bucket("bank")

  val queryTimeout = Play.current.configuration.getInt("queryTimeout").getOrElse(5000)
  val queryDuration = Duration(queryTimeout, "millis")
  val defaultPartitionSize = 50

  def executeQuery[T](query: Query, designName: String, viewName: String)(implicit e: Reads[T]): List[T] = {
    try {
      val future = bucket.find[T](designName, viewName)(query)
      Await.result(future, queryDuration)
    } catch {
      case ex: Exception =>
        Logger.error("Error in execute query. Design name =" + designName + " View name " + viewName, ex)
        throw ex
    }
  }

  def executeAsyncQuery[T](query: Query, designName: String, viewName: String)(implicit r: Reads[T]): Future[List[T]] = {
    try {
      bucket.find[T](designName, viewName)(query)
    } catch {
      case ex: Exception =>
        Logger.error("Error in execute quer. Design name=" + designName + " View name " + viewName, ex)
        throw ex
    }
  }

  def executeAsyncQueryWithIds[T](designName: String, viewName: String, ids: List[String])(implicit r: Reads[T]): Future[List[T]] = {
    val partIds = partition(ids, defaultPartitionSize)
    Future.sequence(partIds.map { i =>
      val query = CouchbaseUtils.createQuery(Some(i), true)
      executeAsyncQuery(query, designName, viewName)
    }).map(l => l.flatten)
  }

  def partition[T](list: List[T], size: Int): List[List[T]] = {
    def part(acc: List[List[T]], l: List[T]): List[List[T]] = {
      if (l.size <= size) l :: acc
      else part(l.take(size) :: acc, l.drop(size))
    }
    part(List.empty[List[T]], list).reverse
  }


  def executeGet[T](key: String)(implicit r: Reads[T]): Option[T] = {
    try {
      Await.result(bucket.get(key), queryDuration)
    } catch {
      case ex: Exception =>
        Logger.error("Error in get from DB. Key = " + key, ex)
        throw ex
    }
  }

  def executeAsyncGet[T](key: String)(implicit r: Reads[T]): Future[Option[T]] = {
    try {
      bucket.get[T](key)
    } catch {
      case ex: Exception =>
        Logger.error("Error in get from DB. Key = " + key, ex)
        throw ex
    }
  }

  def executeSet[T](key: String, value: T)(implicit w: Writes[T]): String = {
    try {
      Await.result(bucket.set(key, value), queryDuration)
    } catch {
      case ex: Exception =>
      Logger.error("Error in set to DB. Key = " + key + "value=" + value, ex)
        throw ex
    }
    key
  }

  def executeAsyncSet[T](key: String, value: T)(implicit w: Writes[T]): Future[Unit] = {
    try {
      bucket.getClass
      bucket.set(key, value, persistTo = PersistTo.MASTER) map {
        case OpResult(false, msg, _, _, _) => throw new Exception(msg.getOrElse(s"Error setting key: $key"))
        case _ => ()
      }

    } catch {
      case ex: Exception =>
        Logger.error("Error in set to DB. Key = " + key + " value=" + value, ex)
        throw ex
    }
  }

  def executeDelete(key: String) = {
    try {
      Await.result(bucket.delete(key), queryDuration)
    } catch {
      case ex: Exception =>
        Logger.error("Error in delete from DB. Key = " + key, ex)
        throw ex
    }
  }

  def checkIfExists(key: String): Boolean = {
    try {
      val stats: Map[String, String] = Await.result(bucket.keyStats(key), queryDuration)
      if (stats.isEmpty) return false
      if (stats.contains("key_vb_state") && stats("key_vb_state") != "active") return false
      true
    } catch {
      case e: Throwable =>
        false
    }
  }

}
