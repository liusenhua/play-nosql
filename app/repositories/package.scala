import scala.concurrent.Future

/**
 * Created by andy on 2/8/16.
 */
package object repositories {
  type RepoResponse[T] = Future[Either[String, T]]

}
