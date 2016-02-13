package controllers

import com.wordnik.swagger.annotations._
import javax.ws.rs.{PathParam, QueryParam}

import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import services.bank.{BankServiceComponent}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import dto._

/**
 * Created by andy on 2/8/16.
 */
@Api(value = "/bank", description = "Bank operations")
class BankController(comp: BankServiceComponent) extends Controller with JsonFormats {
  private val bankService = comp.bankService

  @ApiOperation(value = "Get account", response = classOf[Account], httpMethod = "GET")
  def getAccount(@ApiParam(value = "accountId", required = true) @PathParam("id") accountId: String) = Action.async {
    request =>
      Logger.debug(s"BankController.getAccount, accountId=$accountId")

      bankService.getAccount(accountId) map {
        case DTO(d) => Ok(Json.toJson(d))
        case ErrorDTO(c, m) =>
          Logger.error(s"BankController.getAccount, InternalServerError ($c): $m")
          InternalServerError(m)
      }
  }

  @ApiOperation(value = "Add a account", response = classOf[String], httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Account", required = true, dataType = "Account", paramType = "body")
  ))
  def addAccount() = Action.async {
    request =>
      Logger.debug(s"BankController.addAccount")

      parseRequestJson(request, json => {
        val account = json.as[Account]
        bankService.addAccount(account) map {
          case DTO(d) => Ok(Json.toJson(d))
          case ErrorDTO(c, m) =>
            Logger.error(s"BankController.addAccount, InternalServerError ($c): $m")
            InternalServerError(m)
        }
      })
  }

  @ApiOperation(value = "Update a account", response = classOf[String], httpMethod = "PUT")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "Account", required = true, dataType = "Account", paramType = "body")
  ))
  def updateAccount(@ApiParam(value = "accountId", required = true) @PathParam("id") accountId: String) = Action.async {
    request =>
      Logger.debug(s"BankController.updateAccount, accountId=$accountId")

      parseRequestJson(request, json => {
        val account = json.as[Account]
        bankService.updateAccount(accountId, account) map {
          case DTO(d) => Ok(Json.toJson(d))
          case ErrorDTO(c, m) =>
            Logger.error(s"BankController.updateAccount, InternalServerError ($c): $m")
            InternalServerError(m)
        }
      })
  }

  @ApiOperation(value = "Get all accounts", response = classOf[List[Account]], httpMethod = "GET")
  def getAllAccounts() = Action.async {
    request =>
      Logger.debug(s"BankController.getAllAccounts")

      bankService.getAllAccounts() map {
        case DTO(d) => Ok(Json.toJson(d))
        case ErrorDTO(c, m) =>
          Logger.error(s"BankController.getAllAccounts, InternalServerError ($c): $m")
          InternalServerError(m)
      }
  }

  @ApiOperation(value = "Get accounts by email", response = classOf[List[Account]], httpMethod = "GET")
  def getAccountsByEmail(@ApiParam(value = "email", required = true) @QueryParam("email") email: String) = Action.async {
    request =>
      Logger.debug(s"BankController.getAccountsByEmail, emial = $email")

      bankService.getAccountsByEmail(email) map {
        case DTO(d) => Ok(Json.toJson(d))
        case ErrorDTO(c, m) =>
          Logger.error(s"BankController.getAccountsByEmail, InternalServerError ($c): $m")
          InternalServerError(m)
      }
  }
}
