package repositories.bank

import dto.Account
import repositories.RepoResponse

/**
 * Created by andy on 2/8/16.
 */
trait BankRepoComponent {
  val bankRepo: BankRepo

  trait BankRepo {
    def getAccount(accountId: String): RepoResponse[Account]

    def addAccount(account: Account): RepoResponse[String]

    def updateAccount(accountId: String, account: Account): RepoResponse[String]

    def getAllAccounts(): RepoResponse[List[Account]]

    def getAccountsByEmail(email: String): RepoResponse[List[Account]]
  }
}
