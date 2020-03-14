package models

import java.io.File

import play.api.data.Form
import play.api.data.Forms._

case class BasicForm(name: String)

object BasicForm {
  val form: Form[BasicForm] = Form(
    mapping(
      "Stock Symbols" -> text
    )(BasicForm.apply)(BasicForm.unapply)
  )
}


