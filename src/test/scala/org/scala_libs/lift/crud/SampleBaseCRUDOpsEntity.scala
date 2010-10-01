package org.scala_libs.lift.crud

import scala.xml.Text
import net.liftweb.http.SHtml
import net.liftweb.common.{Full, Empty}

class SampleBaseCRUDOpsEntity(var name: Option[String], var age: Option[Int]) {
  private var savedName = name
  private var savedAge = age

  def save() : Boolean = {
    savedName = name
    savedAge = age
    true
  }

  def delete_! : Boolean = {
    savedName = Empty
    savedAge = Empty
    SampleBaseCRUDOpsEntity.instances -= this
    true
  }
}

object SampleBaseCRUDOpsEntity extends BaseCRUDOps[SampleBaseCRUDOpsEntity] {
  var instances : List[SampleBaseCRUDOpsEntity] = Nil

  def create = {
    val entity = new SampleBaseCRUDOpsEntity(Empty, Empty)
    instances = entity :: instances
    entity
  }

  def getListInstances = instances

  def instanceName = "Entity"

  override def pluralName = "Entities"

  def calcFields = Seq(
    Field(Text("Name"), true, true, (entity) => SHtml.text(entity.name.getOrElse(""), s => entity.name=Full(s) ), (entity) => Text(entity.name.getOrElse("(no name)"))),
    Field(Text("Age"), true, true,
      (entity) => SHtml.text(entity.age.map(_.toString).getOrElse(""), s => entity.age=Full(s.toInt)),
      (entity) => Text(entity.age.map(_.toString).getOrElse("(no age)"))))
}