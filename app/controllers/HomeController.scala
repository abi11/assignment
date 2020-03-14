package controllers

import javax.inject.Inject
import javax.inject.Singleton

import play.api.libs.json._
import play.api.mvc._
import models.{BasicForm}
import views.html.index
import views.html.stocks.stockindex





 case class StockDf(symbol: String,
                   regularMarketPrice: Double)
 
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with play.api.i18n.I18nSupport {

 
  def index() = Action{ implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
  
  def simpleForm() = Action{ implicit request: Request[AnyContent] =>
    Ok(views.html.basicForm(BasicForm.form))
  }

  def simpleFormPost() = Action { implicit request =>
    BasicForm.form.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.basicForm(formWithErrors))
      },
      formData => {
		val baseURL  = "https://query2.finance.yahoo.com/v7/finance/quote?symbols="
		val newURL  = baseURL + (formData.toString).substring(10,(formData.toString).length - 1)
		val opString = scala.io.Source.fromURL(newURL).mkString
		val rawJson  = Json.parse(opString)
		val regPrices = rawJson \\ "regularMarketPrice"
		val syms = rawJson \\ "symbol"
		val list =  scala.collection.mutable.ArrayBuffer[StockDf]()
		
		for (i <- 0 until syms.length) {
			list+= new StockDf(syms(i).toString,(regPrices(i).toString).toDouble)
		}
	   Ok(stockindex.render(list.toArray))
	   
      }
    )
  }
}


