package services.bank

import dto.{DTO, ErrorDTO, Account}
import repositories.bank.BankRepoComponent
import services._
import utils.Errors

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by andy on 2/8/16.
 */
trait DefaultBankServiceComponent extends BankServiceComponent {
  this: BankRepoComponent =>

  val bankService = new DefaultBankService

  class DefaultBankService extends BankService {
    def getAccount(accountId: String): ServiceResponse[Account] = {
      val f: ServiceResponse[Account] = bankRepo.getAccount(accountId) map {
        case Right(account) => DTO(account)
        case Left(msg) => ErrorDTO(Errors.GenericRepo, msg)
        case _ => ErrorDTO(Errors.GenericRepo, "unable to get account from repository")
      }

      f recover {
        case e: Throwable => ErrorDTO[Account](Errors.GenericRepo, e.getMessage)
      }
    }

    def addAccount(account: Account): ServiceResponse[String] = {
      val f: ServiceResponse[String] = bankRepo.addAccount(account) map {
        case Right(key) => DTO(key)
        case Left(msg) => ErrorDTO(Errors.GenericRepo, msg)
        case _ => ErrorDTO(Errors.GenericRepo, "unable to add account to repository")
      }

      f recover {
        case e: Throwable => ErrorDTO[String](Errors.GenericRepo, e.getMessage)
      }
    }

    def updateAccount(accountId: String, account: Account): ServiceResponse[String] = {
      val f: ServiceResponse[String] = bankRepo.updateAccount(accountId, account) map {
        case Right(key) => DTO(key)
        case Left(msg) => ErrorDTO(Errors.GenericRepo, msg)
        case _ => ErrorDTO(Errors.GenericRepo, "unable to update account in repository")
      }

      f recover {
        case e: Throwable => ErrorDTO[String](Errors.GenericRepo, e.getMessage)
      }
    }

    def getAllAccounts(): ServiceResponse[List[Account]] = {
      val f: ServiceResponse[List[Account]] = bankRepo.getAllAccounts() map {
        case Right(key) => DTO(key)
        case Left(msg) => ErrorDTO(Errors.GenericRepo, msg)
        case _ => ErrorDTO(Errors.GenericRepo, "unable to query accounts in repository")
      }

      f recover {
        case e: Throwable => ErrorDTO[List[Account]](Errors.GenericRepo, e.getMessage)
      }
    }

    def getAccountsByEmail(email: String): ServiceResponse[List[Account]] = {
      val f: ServiceResponse[List[Account]] = bankRepo.getAccountsByEmail(email) map {
        case Right(accounts) => DTO(accounts)
        case Left(msg) => ErrorDTO(Errors.GenericRepo, msg)
        case _ => ErrorDTO(Errors.GenericRepo, "unable to query accounts in repository")
      }

      f recover {
        case e: Throwable => ErrorDTO[List[Account]](Errors.GenericRepo, e.getMessage)
      }
    }

  }
}
