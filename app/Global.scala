import controllers.{BankController, DefaultController}
import play.api._

import repositories.bank.DefaultBankRepoComponent
import services.bank.DefaultBankServiceComponent
import utils.HttpFetcher

/**
 * Created by andy on 2/7/16.
 */
object Global extends GlobalSettings {
  override def onStart(app: Application): Unit = {
    Logger.info("Application has started")
  }

  override def getControllerInstance[A](clazz: Class[A]) = {
    clazz match {
      case c if c.isAssignableFrom(classOf[DefaultController]) =>
        new DefaultController().asInstanceOf[A]
      case c if c.isAssignableFrom(classOf[BankController]) =>
        new BankController(new DefaultBankServiceComponent with DefaultBankRepoComponent with HttpFetcher {}).asInstanceOf[A]
      case _ => super.getControllerInstance(clazz)
    }
  }

  override def onStop(app: Application): Unit = {
    Logger.info("Application shutdown...")
  }

}
