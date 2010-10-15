package org.scala_libs.lift.crud.jdo

import com.google.appengine.api.datastore.{KeyFactory, Key}

/**
 * A CRUDOps for a GAE JDO entity.
 * @author epabst@gmail.com
 */

trait GaeCRUDOps[Entity <: Any] extends JdoCRUDOps[Key,Entity] {
  override def stringToKey(keyString: String): Key = KeyFactory.stringToKey(keyString)
  override def keyToString(key: Key): String = KeyFactory.keyToString(key)
}
