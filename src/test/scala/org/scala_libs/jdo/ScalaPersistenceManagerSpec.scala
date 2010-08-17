package org.scala_libs.jdo

import org.specs.SpecificationWithJUnit
import com.google.appengine.tools.development.testing.{LocalServiceTestHelper, LocalDatastoreServiceTestConfig}
import test.model.SampleEntity
import com.jcraft.lift.model.Model.pm
import com.jcraft.lift.model.Model
import javax.jdo.JDOHelper

/**
 * Created by IntelliJ IDEA.
 * User: eric
 * Date: Aug 13, 2010
 * Time: 11:02:45 PM
 * To change this template use File | Settings | File Templates.
 */

class ScalaPersistenceManagerSpec extends SpecificationWithJUnit {
  val helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  Model.pmSource = new PersistenceManagerSource(JDOHelper.getPersistenceManagerFactory("transactions-optional")) {
    private var _pm = openPM
    def pm : ScalaPersistenceManager = _pm
    def removePM() = {_pm = openPM}
  }

  "it should handle getObjectsById" in {
    helper.setUp
    val entity = new SampleEntity("sample")
    pm.makePersistent(entity)
    val entityId = entity.id

    Model.closePM
    val persistedEntities = pm.getObjectsById(classOf[SampleEntity], List(entityId))
    persistedEntities.size must beEqual(1)
  }

  doAfter {
    Model.closePM
    helper.tearDown
  }
}