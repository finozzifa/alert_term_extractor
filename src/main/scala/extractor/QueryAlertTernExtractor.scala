package extractor

import utils.{ApiUtil, matchingUtil}
import models._
import upickle.default
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.nio.file.{Files, Paths, StandardOpenOption}

object QueryAlertTernExtractor {

  private val logger: Logger = LogManager.getLogger(ApiUtil.getClass)

  // Replace with your actual API key
  private val apiKey: String = "xxx"
  private val queryTermEndpoint: String = s"xxx"
  private val alertEndpoint: String = s"xxx"
  implicit val queryTermReader: default.ReadWriter[queryTerm] = upickle.default.macroRW[queryTerm]
  implicit val contentTermReader: default.ReadWriter[contentTerm] = upickle.default.macroRW[contentTerm]
  implicit val alertTermReader: default.ReadWriter[alertTerm] = upickle.default.macroRW[alertTerm]

  def main(args: Array[String]): Unit = {

    // Get the query terms
    val queryTermResponse = ApiUtil.getRequest(apiKey, queryTermEndpoint) match {
      case Some(response) =>
        ApiUtil.parseResponse[Seq[queryTerm]](response)
      case None =>
        logger.warn("Failed to get response for queryTermEndpoint")
        Seq.empty[queryTerm]
    }

    // Get the alert terms
    val alertTermResponse = ApiUtil.getRequest(apiKey, alertEndpoint) match {
      case Some(response) =>
         ApiUtil.parseResponse[Seq[alertTerm]](response)
      case None =>
        logger.warn("Failed to get response for alertEndpoint")
        Seq.empty[alertTerm]
    }

    // Determine in which alert a query term text occurs. Returns a list without duplicates
    val matches = matchingUtil.findMatches(queryTermResponse, alertTermResponse)

    // Write the list to the file
    writeListToFile(matches, "alert.txt")

  }

  /**
   * Writes a sequence of tuples to a file, where each tuple contains optional values.
   *
   * Each tuple in the sequence is represented as a line in the output file. The method
   * converts the sequence of tuples into a string, with each tuple's elements separated by a
   * system-specific line separator. The resulting string is then converted to a byte array
   * and written to the specified file.
   * @param lines A sequence of tuples
   * @param filePath The path to the file where the data will be written. If the file already exists, it will be overwritten.
   * @throws IOException If an I/O error occurs while writing to the file.
   */
  private def writeListToFile(lines: Seq[(Option[String], Option[String], Option[String], Option[Boolean])], filePath: String): Unit = {

    // Convert the list to a sequence of bytes
    val bytes = lines.mkString(System.lineSeparator()).getBytes

    // Write the bytes to the file
    Files.write(Paths.get(filePath), bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE)

  }

}
