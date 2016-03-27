package com.twitter_package.database

import reactivemongo.api.MongoDriver

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global


trait MongoDr {

	println("1")
    val driver = new MongoDriver
    println("2")
    val connection = driver.connection(List("localhost"))
    println("3")
    // Gets a reference to the database "plugin"
    val db = connection("twitter")
    println("4")

}
