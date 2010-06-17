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

package org.scala_libs.jdo.lift

import javax.jdo.PersistenceManager
import net.liftweb.http.{S, RequestVar}
import org.scala_libs.jdo.{ContextPMFactory, LocalPMFactory}

class RequestPMFactory(unitName : String) extends ContextPMFactory(unitName) {

  private object requestPM extends RequestVar[PersistenceManager]({
    val _pm = underlying.openPM
    S.addCleanupFunc(() => {
      underlying.closePM(_pm)
    })
    _pm
  })

  def getOrOpenPMAndRegisterCleanup : PersistenceManager = {
    requestPM.is
  }
}