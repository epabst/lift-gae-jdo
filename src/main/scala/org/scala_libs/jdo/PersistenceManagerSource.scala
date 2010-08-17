package org.scala_libs.jdo

import javax.jdo.PersistenceManagerFactory

/**
 * The source of a ScalaPersistenceManager a Thread or HTTP Request.
 * @author Eric Pabst (epabst@gmail.com)
 * Date: Aug 16, 2010
 * Time: 8:29:06 PM
 */
abstract class PersistenceManagerSource(factory : => PersistenceManagerFactory) {
  def pm : ScalaPersistenceManager

  /**
   * Should release the pm so that the next time it is asked for, a new one will be created.
   * It will already be closed since it will be called from closePM.
   */
  protected def removePM

  protected def openPM : ScalaPersistenceManager = {
    new ScalaPersistenceManager(factory.getPersistenceManager)
  }

  /**
   * Closes the current pm, and clears it so that the next time one is requested, a new one will be created.
   */
  def closePM = {
    pm.javaPM.close
    removePM
  }

  def doWith[R](f : => R) : R = {
    try { f() }
    finally { closePM }
  }

  def inTX[A](f: () => A):A={
    withPM{
      _pm = pm
      val tx = pm.javaPM.currentTransaction
      try{
        tx.begin
        f() match {
          case a => tx.commit; a
        }
      }
      finally{
        if(tx.isActive){
          tx.rollback
        }
      }
    }
  }


}