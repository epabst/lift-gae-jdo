package org.scala_libs.jdo

import javax.jdo.PersistenceManager

/**
 * A {@link ScalaPMFactory} that keeps a {@link PersistenceManager} open somehow from the context of the current thread.
 */
abstract class ContextPMFactory(val unitName : String)
  extends ScalaPMFactory {
  val underlying = new LocalPMFactory(unitName)

  def pm : PersistenceManager = getOrOpenPMAndRegisterCleanup

  /**
   * Gets (opening one if necessary) the PersistenceManager for the current context.
   * It may ought to use <code>underlying</code> to open a Scaa.
   * It should also make sure that when the current context ends, it is closed properly,
   * and that later calls to this method open a new one.
   */
  def getOrOpenPMAndRegisterCleanup : PersistenceManager

  protected def openPM () : PersistenceManager = pm

  //does nothing.  It needs to be closed when the context is completed somehow
  protected[jdo] def closePM (pm : PersistenceManager) : Unit = {}

}