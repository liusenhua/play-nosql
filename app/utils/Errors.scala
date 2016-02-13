package utils

object Errors extends Enumeration {
  val NoError = -1
  val Generic = 0
  val InvalidInput = 1
  val InvalidEmail = 2
  val InvalidPassword = 3
  val InvalidMode = 4
  val GenericValidation = 5
  val NotFound = 6
  val GenericRepo = 7
}
