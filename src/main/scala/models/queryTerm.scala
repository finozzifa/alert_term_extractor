package models

case class queryTerm
(
  id: Option[String] = None,
  target: Option[Int] = None,
  text: Option[String] = None,
  language: Option[String] = None,
  keepOrder: Option[Boolean] = None
)
