package utils

import requests._
import upickle.default._
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object ApiUtil {

  private val logger: Logger = LogManager.getLogger(ApiUtil.getClass)

  /**
   * Performs an API GET request.
   *
   * @param apiKey      The API key.
   * @param apiEndpoint The API endpoint.
   * @return Option[Response] The requests.Response object wrapped in an Option.
   * @throws Exception if API request fails.
   */
  def getRequest(apiKey: String, apiEndpoint: String): Option[Response] = {
    val apiPath = s"$apiEndpoint$apiKey" // Using string interpolation for better readability
    try {
      val response = requests.get(apiPath)
      if (response.statusCode == 200) {
        logger.info(s"Request for endpoint $apiEndpoint was successful. The HTTP status is ${response.statusCode}")
        Some(response) // Return the response wrapped in Some
      } else {
        logger.warn(s"Request failed with HTTP status ${response.statusCode}")
        None // Return None for non-200 responses
      }
    } catch {
      case e: Exception =>
        logger.error(s"Unable to perform the API call for the endpoint $apiEndpoint", e) // Log the exception with stack trace
        throw e
    }
  }

  /**
   * Parses the JSON response body into an instance of type T.
   *
   * @param response The response object containing JSON text.
   * @tparam T The target type to deserialize into, with an implicit Reader available.
   * @return The deserialized object of type T.
   * @throws Exception if JSON parsing fails.
   */
  def parseResponse[T: Reader](response: Response): T = {
    try {
      read[T](response.text())
    } catch {
      case e: Exception =>
        logger.error(s"Failed to parse response: ${e.getMessage}", e)
        throw e
    }
  }

}
