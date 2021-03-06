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
import javax.jdo.annotations._
import com.google.appengine.api.datastore.Key
import net.liftweb.http.SHtml
import xml.Text
import com.jcraft.lift.snippet.BookOps
import org.scala_libs.jdo.criterion.eqC
import org.scala_libs.lift.crud.jdo.GaeCRUDOps

/**
  An author is someone who writes books.
*/
@PersistenceCapable(identityType = IdentityType.APPLICATION,
                    detachable="true")
class Author {

  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  var id : Key = _

  @Persistent
  var name : String = ""

  @Persistent(mappedBy = "author", defaultFetchGroup="true")
  var books : java.util.List[Book] = new java.util.LinkedList[Book]

  def findBooks = {
    Book.query.where(eqC("author", this)).resultList
  }
}

object Author extends GaeCRUDOps[Author] {
  def instanceName = "Author"
  val entityClass = classOf[Author]

  def calcFields = Seq(Field(Text("Name"), true, true, (author : Author) => SHtml.text(author.name,  author.name=_ ), (author: Author) => Text(author.name)),
    Field(Text("Books published"), false, true, _ => Nil, (author: Author) => SHtml.link("/books/search", () => BookOps.resultVar(author.findBooks), Text(author.books.size.toString))))
}
