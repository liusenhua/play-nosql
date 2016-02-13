package controllers

import play.api._
import play.api.mvc._

class DefaultController extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
