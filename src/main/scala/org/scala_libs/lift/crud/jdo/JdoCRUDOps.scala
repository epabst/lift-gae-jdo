package org.scala_libs.lift.crud.jdo

import org.scala_libs.jdo.{JdoConfig, ScalaPersistenceManager}
import org.scala_libs.lift.crud.BaseCRUDOps

/**
 * A CRUDOps for a JDO entity.
 * @author epabst@gmail.com
 */

trait JdoCRUDOps[IdType <: Object,Entity <: Any] extends BaseCRUDOps[Entity] {
  /** entityClass is needed for the default implementations of create and findAll */
  val entityClass: Class[Entity]
  def create = entityClass.newInstance
  def save(entity: Entity) : Boolean = { pm.makePersistent(entity); true }
  def delete_!(entity: Entity) : Boolean = { pm.deletePersistent(entity); true }
  def query = pm.from(entityClass)
  def findById(id: IdType): Option[Entity] = pm.getObjectById[Entity](entityClass, id)
  def findAll = query.resultList
  def getListInstances = findAll

  //this default pm may be overridden, presumably coming from some PersistenceManagerSource set by Boot or test setup 
  def pm: ScalaPersistenceManager = JdoConfig.pm
}
