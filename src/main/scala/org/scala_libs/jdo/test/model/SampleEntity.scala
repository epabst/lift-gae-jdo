package org.scala_libs.jdo.test.model

import com.google.appengine.api.datastore.Key
import javax.jdo.annotations._

//todo move this to the test directory after figuring out how to enhance test code
@PersistenceCapable{val identityType = IdentityType.APPLICATION, val detachable = "true"}
class SampleEntity(@Persistent var name : String) {
  @PrimaryKey
  @Persistent{val valueStrategy = IdGeneratorStrategy.IDENTITY}
  var id : Key = _
}