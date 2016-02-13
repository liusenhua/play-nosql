package dto

import play.api.libs.json._

import utils.Errors

/**
 * generic class representing the base for all the responses
 * @tparam T
 */
sealed abstract class ResponseDTO[T] {
  def isError: Boolean
}

/**
 * Simple response wrapping a string
 */
case class StringResponse(value: String)

/**
 * container for the error response
 * @param errCode
 * @param errMessage
 * @tparam T
 */
case class ErrorDTO[T](errCode: Int = -1, errMessage: String = "") extends ResponseDTO[T] {
  def isError = true
}

case class DTO[T](d: T) extends ResponseDTO[T] {
  def isError = false

  /**
   * helper for the getting the inner value
   * @return the object of type T contained by this wrapper
   */
  def get() = d
}

/**
 * implementaion of json combinators for the basic response
 */
trait BaseFormats {
  implicit def genericFormat[T](implicit fmt: Writes[T]): Writes[ResponseDTO[T]] =
    new Writes[ResponseDTO[T]] {
      def writes(o: ResponseDTO[T]) = o match {
        case DTO(value) => fmt.writes(value).as[JsObject] ++ Json.obj("er" -> Errors.NoError, "erMessage" -> "")
        case ErrorDTO(code, message) => Json.obj("er" -> JsNumber(code), "errMessage" -> message)
      }
    }

  implicit val simpleWrites = new Writes[StringResponse] {
    def writes(d: StringResponse): JsValue = {
      Json.obj(
      "value" -> d.value
      )
    }
  }

}
