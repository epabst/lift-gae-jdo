package org.scala_libs.lift.crud

import net.liftweb.common.Box

trait InMemoryCRUDOps[Entity] extends KeyedCRUDOps[Long,Entity] {
  var instances : Map[Long,Entity] = Map.empty

  private var nextId: Long = 1

  def create(id: Long): Entity

  def create() = {
    val entity = create(nextId)
    instances = instances + (nextId -> entity)
    nextId += 1
    entity
  }

  def findByKey(key: Long): Box[Entity] = instances.get(key)

  def getListInstances = instances.values.toList

  def save(entity: Entity) : Boolean = true

  def delete_!(entity: Entity) : Boolean = {
    instances = instances.filter{(entry) => (entry._2 != entity)}
    true
  }
}
