package dto

import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
 * Created by andy on 2/8/16.
 */
trait JsonFormats extends BaseFormats {
  implicit lazy val readerAccount: Reads[Account] = (
    (__ \ "account_number").read[Long] and
      (__ \ "balance").read[Float] and
      (__ \ "firstname").read[String] and
      (__ \ "lastname").read[String] and
      (__ \ "age").read[Int] and
      (__ \ "gender").read[String] and
      (__ \ "address").read[String] and
      (__ \ "employer").read[String] and
      (__ \ "email").read[String] and
      (__ \ "city").read[String] and
      (__ \ "state").read[String]
    )(Account)

  implicit lazy val writeAccount = new Writes[Account]  {
    def writes(account:Account): JsValue = {
      Json.obj(
        "account_number" -> account.accountId,
        "balance" -> account.balance,
        "firstname" -> account.firstName,
        "lastname" -> account.lastName,
        "age" -> account.age,
        "gender" -> account.gender,
        "address" -> account.address,
        "employer" -> account.employer,
        "email" -> account.email,
        "city" -> account.city,
        "state" -> account.state
      )
    }
  }

}
