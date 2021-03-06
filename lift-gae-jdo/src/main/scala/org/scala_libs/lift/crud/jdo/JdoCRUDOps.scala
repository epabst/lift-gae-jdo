package org.scala_libs.lift.crud.jdo

import org.scala_libs.jdo.{JdoConfig, ScalaPersistenceManager}
import _root_.net.liftweb.common.Loggable
import net.liftweb.common.Box
import org.scala_libs.lift.crud.KeyedCRUDOps

/**
 * A CRUDOps for a JDO entity.
 * @author epabst@gmail.com
 */

trait JdoCRUDOps[IdType <: Object,Entity <: Any] extends KeyedCRUDOps[IdType,Entity] with Loggable {
  /** entityClass is needed for the default implementations of create and findAll */
  val entityClass: Class[Entity]
  def create = { val entity = entityClass.newInstance; logger.debug("created " + entity); entity }
  override def useObjectFromPriorRequest(entity: Entity) = { logger.debug("using " + entity); super.useObjectFromPriorRequest(pm.makePersistent(entity)) }
  def save(entity: Entity) : Boolean = { logger.debug("saving " + entity); pm.makePersistent(entity); true }
  def delete_!(entity: Entity) : Boolean = { logger.debug("deleting " + entity); pm.deletePersistent(entity); true }
  def query = pm.from(entityClass)
  def findByKey(id: IdType): Box[Entity] = { val entity: Box[Entity] = pm.getObjectById[Entity](entityClass, id); logger.debug("findByKey returned " + entity); entity }
  def findAll = { val results = query.resultList; logger.debug("findAll returned " + results); results }
  def getListInstances = findAll

  //this default pm may be overridden, presumably coming from some PersistenceManagerSource set by Boot or test setup 
  def pm: ScalaPersistenceManager = JdoConfig.pm
}
