package com.twitter_package

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future


import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

import scala.util.{ Failure, Success }

import scala.concurrent.duration.FiniteDuration

import com.twitter_package.models.Models
import com.twitter_package.models.Models._

object PhawzyTest extends App { 

	val db= new DatabaseDriver
  var id:Any = _
  //var userCreated:Either[UserCreated, UserAlreadyExists] = _
	val username = readLine("What's your username? ")
	val email = readLine("What's your email? ")
	val password = readLine("What's your password? ")
	val user=User(username=username,email=email,password=password)
	
  /*
  db.newUser(user).onComplete {
    case Failure(e) => throw e
    case Success(m) => m match {
      case UserCreated(uid)=> id=uid ;val userCreated=UserCreated(uid); println(s"id: ${userCreated.id}")
      case _ => println("nothing")
    }
        
  }

   */

//	val userFuture = db.findUserByEmail("m")
/*	userFuture.onComplete {
      case Failure(e) => throw e
      case Success(userOption) =>
      val user=userOption.orNull
      if(user == null){
      	println("no such user")
      }else{
		val id=userOption.get.getAs[BSONObjectID]("_id").get.toString()
      	println(s"user: ${id}")
      }
      
    }
	*/
	
	println("6")
}