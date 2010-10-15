package org.scala_libs.jdo

import javax.jdo.PersistenceManagerFactory

/**
 * The source of a ScalaPersistenceManager a Thread or HTTP Request.
 * @author Eric Pabst (epabst@gmail.com)
 * Date: Aug 16, 2010
 * Time: 8:29:06 PM
 */
abstract class PersistenceManagerSource(factory : => PersistenceManagerFactory) {
  private lazy val _factory : PersistenceManagerFactory = factory
  def pm : ScalaPersistenceManager

  /**
   * Should release the pm so that the next time it is asked for, a new one will be created.
   * It will already be closed since it will be called from closePM.
   */
  protected def removePM()

  protected def openPM() : ScalaPersistenceManager = {
    new ScalaPersistenceManager(_factory.getPersistenceManager)
  }

  /**
   * Closes the current pm, and clears it so that the next time one is requested, a new one will be created.
   */
  def closePM() = {
    pm.javaPM.close
    removePM
  }

  def withPM[R](f : () => R) : R = {
    try { f() }
    finally { closePM }
  }

  def inTX[R](f : () => R):R={
    val tx = pm.javaPM.currentTransaction
    try{
      tx.begin
      val result = f()
      tx.commit
      result
    }
    finally{
      if(tx.isActive){
        tx.rollback
      }
    }
  }


}