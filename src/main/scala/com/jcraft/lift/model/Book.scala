/*
 * Copyright 2009 ymnk, JCraft,Inc.
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

package com.jcraft.lift.model

import _root_.com.google.appengine.api.datastore.Key
import _root_.com.google.appengine.api.datastore.KeyFactory._

import java.util.Date
import javax.jdo.annotations._
import org.scala_libs.lift.crud.jdo.JdoCRUDOps
import java.text.ParseException
import net.liftweb.http.{S, SHtml}
import net.liftweb.common.{Full, Box}
import xml.{NodeSeq, Text, UnprefixedAttribute, Null}

@PersistenceCapable{val identityType = IdentityType.APPLICATION,
                    val detachable="true"}
class Book {

  @PrimaryKey
  @Persistent{val valueStrategy = IdGeneratorStrategy.IDENTITY}
  var id : Key = _

  @Persistent
  var title : String = ""

  @Persistent
  var published : Date = new Date()

  @Persistent
  var genre : String = Genre.unknown.toString

  @Persistent{ val defaultFetchGroup="true"}
  var author : Author = _
}

object Book extends JdoCRUDOps[Key,Book] {
  def instanceName = "Book"
  val entityClass = classOf[Book]

  private val formatter = new java.text.SimpleDateFormat("yyyyMMdd")

  import SHtml.{text, select}

  def calcFields = {
    def setDate (input : String, toSet : Book) {
      try { toSet.published=formatter.parse(input) }
      catch { case pe : ParseException => S.error("Error parsing the date") }
    }

    Seq(
      Field(Text("Title"), true, true, (book: Book) => text(book.title, book.title=_), (book: Book) => Text(book.title)),
      Field(Text("Published"), true, true,
        (book: Book) => {
          val elem = text(formatter.format(book.published), setDate(_, book))
          elem % new UnprefixedAttribute("class", Text("datePicker"), Null)},
        (book: Book) => Text(formatter.format(book.published))),
      Field(Text("Genre"), true, true,
        (book: Book) => select(Genre.getNameDescriptionList, (Box.legacyNullTest(book.genre).map(_.toString) or Full("")),
          choice => book.genre = Genre.valueOf(choice).getOrElse("").toString),
        (book: Book) => Text(if(book.genre != null) book.genre.toString else "")),
      Field(Text("Author"), true, true, authorFieldForForm, (book: Book) => Text(book.author.name)))
  }

  def authorFieldForForm(book: Book): NodeSeq = {
    val choices = Author.findAll.map(author => (keyToString(author.id) -> author.name))
    val default = Box.legacyNullTest(book.author).map(author => keyToString(author.id))
    if(book.author==null) { select(choices, default, (id) => book.author = Author.findById(stringToKey(id)).getOrElse(null)) } else Text(book.author.name)
  }

  override def pageWrap(body: NodeSeq) = <lift:surround with="defaultWithDatePicker" at="content">{ body }</lift:surround>
}