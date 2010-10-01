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
import org.scala_libs.lift.crud.BaseCRUDOps
import net.liftweb.http.SHtml
import xml.Text
import com.jcraft.lift.snippet.BookOps
import org.scala_libs.jdo.criterion.eqC
import Model.pm

/**
  An author is someone who writes books.
*/
@PersistenceCapable{val identityType = IdentityType.APPLICATION,
                    val detachable="true"}
class Author {

  @PrimaryKey
  @Persistent{val valueStrategy = IdGeneratorStrategy.IDENTITY}
  var id : Key = _

  @Persistent
  var name : String = ""

  @Persistent{val mappedBy = "author",
              val defaultFetchGroup="true"}
  var books : java.util.List[Book] = new java.util.LinkedList[Book]

  def findBooks = {
    pm.from(classOf[Book]).where(eqC("author", this)).resultList
  }
}

object Author extends BaseCRUDOps[Author] {
  def instanceName = "Author"
  def create() = new Author
  def save(author: Author) : Boolean = { pm.makePersistent(author); true }
  def delete_!(author: Author) : Boolean = { pm.deletePersistent(author); true }
  def findAll = pm.from(classOf[Author]).resultList
  def getListInstances = findAll

  def calcFields = Seq(Field(Text("Name"), true, true, (author : Author) => SHtml.text(author.name,  author.name=_ ), (author: Author) => Text(author.name)),
    Field(Text("Books published"), false, true, _ => Nil, (author: Author) => SHtml.link("/books/search", () => BookOps.resultVar(author.findBooks), Text(author.books.size.toString))))
}
