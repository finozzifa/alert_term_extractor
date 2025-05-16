package utils

import org.scalatest.funsuite.AnyFunSuite
import upickle.default

import scala.io.Source
import models.{alertTerm, contentTerm, queryTerm}

class matchingUtilTest extends AnyFunSuite {

  implicit val queryTermReader: default.ReadWriter[queryTerm] = upickle.default.macroRW[queryTerm]
  implicit val contentTermReader: default.ReadWriter[contentTerm] = upickle.default.macroRW[contentTerm]
  implicit val alertTermReader: default.ReadWriter[alertTerm] = upickle.default.macroRW[alertTerm]

  test("find the matches between query text and alerts") {

    // Get example query terms
    val queryExampleFileName = "src/test/resources/queryExample.json"
    val queryExampleString = Source.fromFile(queryExampleFileName).getLines().mkString
    val queryTermSeq = upickle.default.read[Seq[queryTerm]](queryExampleString)

    // Get example alert terms
    val alertExampleFileName = "src/test/resources/alertExample.json"
    val alertExampleString = Source.fromFile(alertExampleFileName).getLines().mkString
    val alertTermSeq = upickle.default.read[Seq[alertTerm]](alertExampleString)

    // Expected matches
    val expectedMatches = List((Some("101"), Some("id_1"), Some("car"), Some(true)), (Some("102"), Some("id_1"), Some("table"), Some(true)), (Some("103"), Some("id_1"), Some("new phone"), Some(false)), (Some("104"), Some("id_1"), Some("tHere"), Some(true)), (Some("103"), Some("id_2"), Some("new phone"), Some(false)), (Some("104"), Some("id_2"), Some("tHere"), Some(true)))
    // Returned matches
    val outputMatches = matchingUtil.findMatches(queryTermSeq, alertTermSeq)
    assert(expectedMatches == outputMatches)
  }

}
