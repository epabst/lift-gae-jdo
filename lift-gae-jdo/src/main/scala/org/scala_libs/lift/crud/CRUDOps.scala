/* 
 * Copyright © 2009, Derek Chen-Becker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
/* NOTE: This file is MODIFIED from its original CRUDLike.scala content at http://github.com/dchenbecker/LiftTicket */
package org.scala_libs.lift.crud

import scala.xml.{NodeSeq,Text}

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.mapper._
import net.liftweb.sitemap._
import net.liftweb.util._
import Helpers._

/**
 * This trait is intended to be a flexible way to mix CRUD operations into
 * an existing MetaMapper. In addition to normal CRUD on the MetaMapper's
 * defined fields, this allows you to create synthetic fields and simplifies
 * control over access to various functions.
 */
trait CRUDOps[KeyType, MapperType <: KeyedMapper[KeyType,MapperType]] extends BaseCRUDOps[MapperType] {
  self : MapperType with KeyedMetaMapper[KeyType,MapperType] =>

  /** This calculates the fields for this CRUDOps to work with */
  def calcFields : Seq[Field] = mappedFields.map(defaultField)

  /** A default generator for a field instance with control over view/edit/list
   */
  def defaultField (field : BaseMappedField,
                    edit : Boolean,
                    view : Boolean,
                    list : Boolean) : Field = {
    val displayFunc = {
      instance : MapperType => (instance.fieldByName(field.name).map{mf : MappedField[_,MapperType] => mf.asHtml}) openOr Text("Could not generate form for " + field.name)
    }

    Field(Text(field.displayName),
          edit, view, list,
          { instance =>
            (instance.fieldByName(field.name).flatMap{mf : MappedField[_,MapperType] => mf.toForm}) openOr Text("Could not generate form for " + field.name)},
          displayFunc,
          displayFunc
    )
  }


  /**
   * A default Field generator based on Mapper fields
   */
  def defaultField (field : BaseMappedField) : Field =
    defaultField(field,
                 field.dbIncludeInForm_?,
                 field.dbDisplay_?,
                 field.dbDisplay_?)

  /**
   * This method may be overridden to control which instances are shown in
   * the list view.
   */
  def getListInstances : List[MapperType] = findAll

  //necessary?
  //def create : MapperType = getSingleton.create
}
