package services.bank

import dto.Account
import services._

trait BankServiceComponent {
  val bankService: BankService

  trait BankService {
    def getAccount(accountId: String): ServiceResponse[Account]

    def addAccount(account: Account): ServiceResponse[String]

    def updateAccount(accountId: String, account: Account): ServiceResponse[String]

    def getAllAccounts(): ServiceResponse[List[Account]]

    def getAccountsByEmail(email: String): ServiceResponse[List[Account]]
  }
}
