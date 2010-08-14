package org.scala_libs.jdo

import org.specs.SpecificationWithJUnit
import com.google.appengine.tools.development.testing.{LocalServiceTestHelper, LocalDatastoreServiceTestConfig}
import ModelForTesting.pm
import test.model.SampleEntity

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Aug 13, 2010
 * Time: 11:02:45 PM
 * To change this template use File | Settings | File Templates.
 */

class ScalaPersistenceManagerSpec extends SpecificationWithJUnit {
  val helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

  "it should handle getObjectsById" in {
    helper.setUp
    val entity = new SampleEntity("sample")
    pm.makePersistent(entity)
    val entityId = entity.id

    ModelForTesting.closePM
    val persistedEntities = ModelForTesting.getObjectsById(classOf[SampleEntity], List(entityId))
    persistedEntities.size must beEqual(1)
  }

  doAfter {
    ModelForTesting.closePM
    helper.tearDown
  }
}