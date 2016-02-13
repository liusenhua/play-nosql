import dto.ResponseDTO
import scala.concurrent.Future

package object services {
  type ServiceResponse[T] = Future[ResponseDTO[T]]
}