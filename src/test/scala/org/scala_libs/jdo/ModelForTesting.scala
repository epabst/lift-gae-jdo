package org.scala_libs.jdo

import javax.jdo.PersistenceManager

/**
 * A model for use with testing
 * @author pabstec
 */

object ModelForTesting extends ContextPMFactory with ScalaPersistenceManager {
  val factory = this
  val localPMFactory = new LocalPMFactory("transactions-optional")
  private object localPM extends ThreadLocal[PersistenceManager]

  def getOrOpenPMAndRegisterCleanup : PersistenceManager = {
    var persistenceManager = localPM.get()
    if (persistenceManager == null) {
      persistenceManager = localPMFactory.openPM
    }
    persistenceManager
  }

  //to be called by test tear down code
  def closePM : Unit = {
    if (localPM.get != null) {
      closePM(localPM.get)
      localPM.set(null)
    }
  }
}