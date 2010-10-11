package org.scala_libs.jdo.test.model

import com.google.appengine.api.datastore.Key
import javax.jdo.annotations._

//todo move this to the test directory after figuring out how to enhance test code
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
class SampleEntity(@Persistent var name : String) {
  @PrimaryKey
  @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
  var id : Key = _
}