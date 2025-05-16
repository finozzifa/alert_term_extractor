package models

case class contentTerm
(
  text: Option[String] = None,
  `type`: Option[String] = None,
  language: Option[String] = None
)

case class alertTerm
(
  id: Option[String] = None,
  contents: Option[Seq[contentTerm]] = None,
  date: Option[String] = None,
  inputType: Option[String]
)
