package repositories.bank

import dto.{DTO, JsonFormats, Account}
import repositories.couchbase.{BaseCouchbaseRepository, CouchbaseUtils}
import repositories._
import utils.Fetcher

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by andy on 2/8/16.
 */
trait DefaultBankRepoComponent extends BaseCouchbaseRepository with BankRepoComponent {
  this: Fetcher =>

  val bankRepo = new DefaultBankRepo

  val DESIGN_NAME = "account"
  val VIEW_BY_ID = "by_id"
  val VIEW_BY_EMAIL = "by_email"

  class DefaultBankRepo extends BankRepo with JsonFormats {
    def getAccount(accountId: String): RepoResponse[Account] = {
      val f: RepoResponse[Account] = executeAsyncGet[Account](accountId) map {
        case Some(account) => Right(account)
        case _ => throw new Exception("Key not found in DB")
      }
      f
    }

    def addAccount(account: Account): RepoResponse[String] = {
      val id: String = account.accountId.toString
      val f: RepoResponse[String] = executeAsyncSet(id, account) map (_ => Right(account.accountId.toString))
      f
    }

    def updateAccount(accountId: String, account: Account): RepoResponse[String] = {
      checkIfExists(accountId) match {
        case true => executeAsyncSet(accountId, account) map (_ => Right(account.accountId.toString))
        case false => throw new Exception("Key not found in DB")
      }
    }

    def getAllAccounts(): RepoResponse[List[Account]] = {
      val query = CouchbaseUtils.createQuery(None, true)
      val f:RepoResponse[List[Account]] = executeAsyncQuery[Account](query, DESIGN_NAME, VIEW_BY_ID) map {
        case accounts: List[Account] => Right(accounts)
        case _ => throw new Exception("No accounts in DB")
      }
      f
    }

    def getAccountsByEmail(email: String): RepoResponse[List[Account]] = {
      val query = CouchbaseUtils.createQuery(email, true)
      val f:RepoResponse[List[Account]] = executeAsyncQuery[Account](query, DESIGN_NAME, VIEW_BY_EMAIL) map {
        case accounts: List[Account] => Right(accounts)
        case _ => throw new Exception(s"No accounts found by $email in DB")
      }
      f
    }
  }
}
