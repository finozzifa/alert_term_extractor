package utils

import models.{alertTerm, queryTerm}

object matchingUtil {

  /**
   * Finds matches between query terms and alert terms.
   *
   * This method takes two sequences: a sequence of query terms and a sequence of alert terms.
   * It constructs a map of query terms and then checks each alert term to see if any of the
   * query terms are present in the alert's contents. If a match is found, it returns a sequence
   * of tuples containing the term ID, alert ID, and the matched term text.
   *
   * @param queryTerms A sequence of query terms, where each term is represented by an instance of `queryTerm`.
   * @param alertTerms A sequence of alert terms, where each term is represented by an instance of `alertTerm`.
   * @return A sequence of tuples, where each tuple contains:
   *         - An `Option[String]` representing the ID of the matched query term.
   *         - An `Option[String]` representing the ID of the alert term.
   *         - An `Option[String]` representing the matched term text.
   *           The sequence will contain distinct matches only.
   *           If no matches are found, an empty sequence will be returned.
   */
  def findMatches(queryTerms: Seq[queryTerm], alertTerms: Seq[alertTerm]): Seq[(Option[String], Option[String], Option[String], Option[Boolean])] = {

    val queryTermMap = queryTerms.collect {
      case term if term.text.isDefined => (term.id, (term.text, term.keepOrder))
    }.toMap

    alertTerms.flatMap { alert =>
      val alertId = alert.id
      val alertWords = alert.contents
        .toSeq
        .flatten
        .flatMap(_.text)
        .map(_.toLowerCase)

      queryTermMap.collect {
        case (termId, (Some(termText), Some(keepOrder))) =>
          val termWords = termText.split("\\s+").map(_.toLowerCase)

          if (termWords.forall(termWord => alertWords.exists(alertWord => alertWord.contains(termWord)))) {
            if (keepOrder) {
              // Check if termWords appear in the same order in alertWords
              val indices = termWords.map(alertWords.indexOf)
              if (indices.zipWithIndex.forall { case (index, idx) => index == indices.head + idx }) {
                Some(termId, alertId, Option(termText), Option(keepOrder))
              } else {
                None
              }
            } else {
              Some(termId, alertId, Option(termText), Option(keepOrder))
            }
          } else {
            None
          }
      }.flatten
    }.distinct
  }
}