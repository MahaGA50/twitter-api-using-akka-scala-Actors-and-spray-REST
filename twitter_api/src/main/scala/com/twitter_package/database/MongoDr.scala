package com.twitter_package.database

import reactivemongo.api.MongoDriver

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global


trait MongoDr {

    val driver = new MongoDriver
   
    val connection = driver.connection(List("localhost"))
 
    // Gets a reference to the database "plugin"
    val db = connection("twitter")
   

}
