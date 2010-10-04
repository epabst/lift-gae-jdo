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
package com.jcraft.lift.snippet

import _root_.scala.xml.{NodeSeq,Text}
import _root_.net.liftweb.http.{RequestVar,S,SHtml}
import _root_.net.liftweb.util.Helpers
import Helpers._
import S._

import _root_.com.jcraft.lift.model._
import _root_.org.scala_libs.jdo.JdoConfig._
import _root_.org.scala_libs.jdo.criterion._

object BookOps {
  object resultVar extends RequestVar[List[Book]](Nil)
}

class BookOps {
  val formatter = new java.text.SimpleDateFormat("yyyyMMdd")

  object bookVar extends RequestVar[Book](new Book)
  lazy val book = bookVar.is

  def is_valid_Book_? (toCheck : Book) : Boolean ={
    List((if (toCheck.title.length == 0) { 
            S.error("You must provide a title"); false 
           } else true),
         (if (toCheck.published == null) { 
            S.error("You must provide a publish date"); false 
          } else true),
         (if (toCheck.genre == null) { 
            S.error("You must select a genre"); false } else true),
         (if (toCheck.author == null) {
            S.error("You must select an author"); false
          } else true)
        ).forall(_ == true)
  }

  def searchResults (xhtml : NodeSeq) : NodeSeq =
    BookOps.resultVar.is.flatMap(result =>
      bind("result", xhtml, 
           "title" -> Text(result.title), 
           "author" -> Text(result.author.name)))

  def search (xhtml : NodeSeq) : NodeSeq = {
    var title = ""

    def doSearch () = {
      val l = pm.from(classOf[Book])
            .where(geC("title", title),
                   ltC("title", title+"\ufffd"))
            .resultList

      BookOps.resultVar(l)
    }

    bind("search", xhtml,
         "title" -> SHtml.text(title, title = _),
         "run" -> SHtml.submit(?("Search"), doSearch _))
  }
}
