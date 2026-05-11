package de.dnpm.dip.admin.api



final case class Version private (
  major: Int,
  minor: Int,
  patch: Int
)
{
  override def toString = s"$major.$minor.$patch"
}


object Version
{

  // RegEx Source: https://semver.org/
  private val semVer =
    raw"(?<major>0|[1-9]\d*)\.(?<minor>0|[1-9]\d*)\.(?<patch>0|[1-9]\d*)(?:-(?<prerelease>(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\.(?:0|[1-9]\d*|\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\+(?<buildmetadata>[0-9a-zA-Z-]+(?:\.[0-9a-zA-Z-]+)*))?".r


  def apply(version: String): Version = 
    semVer.findFirstMatchIn(version) match {
      case Some(m) => 
        Version(
          m.group("major").toInt,
          m.group("minor").toInt,
          m.group("patch").toInt
        )
      case None => ???
    }

  implicit val ordering: Ordering[Version] =
    Ordering[String].on[Version](_.toString)
}
