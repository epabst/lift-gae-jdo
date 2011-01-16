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

package org.scala_libs.jdo

import net.liftweb.http.{S, RequestVar}
import javax.jdo.PersistenceManagerFactory

class RequestPersistenceManagerSource(factory : => PersistenceManagerFactory) extends PersistenceManagerSource(factory) {

  private object requestPM extends RequestVar[ScalaPersistenceManager]({
    val _pm = openPM
    S.addCleanupFunc(() => {
      closePM
    })
    _pm
  })

  def pm : ScalaPersistenceManager = {
    requestPM.is
  }

  def removePM() = {
    //todo verify that a new one automatically gets created if requested after remove has been called.
    requestPM.remove
  }
}