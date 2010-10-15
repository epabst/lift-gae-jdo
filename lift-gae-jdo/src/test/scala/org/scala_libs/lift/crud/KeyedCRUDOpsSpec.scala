package org.scala_libs.lift.crud

import org.specs.SpecificationWithJUnit
import scala.xml.Text
import net.liftweb.common.{Box, Full, Empty}
import net.liftweb.http.{LiftSession, S, SHtml}
import net.liftweb.util.StringHelpers

/**
 * A behavior specification for {@link KeyedCRUDOps}.
 * @author pabstec
 */

class KeyedCRUDOpsSpec extends SpecificationWithJUnit {
  val session : LiftSession = new LiftSession("", StringHelpers.randomString(20), Empty)
  val pet1 = Pet.create(); pet1.name = Full("Spot")
  val pet2 = Pet.create(); pet2.name = Full("Fluffy")
  Pet.save(pet1)
  Pet.save(pet2)

  "it should handle selectEntity" in {
    S.initIfUninitted(session) {
      val html = Pet.selectEntity(Full(pet2), _.id, _.name.getOrElse(""), (choice: Box[Pet]) => {})
      println("select html=" + html)
      html.toString.contains("Spot") must beTrue
      html.toString.contains("Fluffy") must beTrue
    }
  }

  "it should handle selectEntity with no default" in {
    S.initIfUninitted(session) {
      val html = Pet.selectEntity(Empty, _.id, _.name.getOrElse(""), (choice: Box[Pet]) => {})
      println("select html=" + html)
      html.toString.contains("Spot") must beTrue
      html.toString.contains("Fluffy") must beTrue
    }
  }

  "it should handle selectEntityId" in {
    S.initIfUninitted(session) {
      val html = Pet.selectEntityId(Full(pet2.id), _.id, _.name.getOrElse(""), (choice: Long) => {})
      println("select html=" + html)
      html.toString.contains("Spot") must beTrue
      html.toString.contains("Fluffy") must beTrue
    }
  }
}

class Pet(val id: Long, var name: Option[String])

object Pet extends InMemoryCRUDOps[Pet] {
  def instanceName = "Pet"

  def create(id: Long): Pet = new Pet(id, Empty)

  def calcFields = Seq(
    Field(Text("Name"), true, true, (entity) => SHtml.text(entity.name.getOrElse(""), s => entity.name=Full(s) ), (entity) => Text(entity.name.getOrElse("(no name)"))))
}