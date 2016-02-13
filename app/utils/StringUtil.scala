package utils

/**
 * Created by andy on 2/8/16.
 */
object StringUtil {
  def isEmpty(str: String): Boolean = {
    if (str == null || str.isEmpty || str.toLowerCase == "undefined") true
    else false
  }

  def isNotEmpty(str: String): Boolean = {
    !isEmpty(str)
  }
}
