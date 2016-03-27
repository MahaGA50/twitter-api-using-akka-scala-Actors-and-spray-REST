package com.twitter_package.database

import reactivemongo.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future


import reactivemongo.bson._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.api.QueryOpts
import reactivemongo.core.commands.Count
import reactivemongo.bson.{BSONDocumentWriter, BSONDocument, BSONDocumentReader, BSONObjectID}

import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import scala.util.{ Failure, Success , Try}

import scala.concurrent.duration._
import scala.concurrent.Await



import com.twitter_package.models.UserModel


class UserDr extends MongoDr {

	import com.twitter_package.models.UserModel._
	import com.twitter_package.models.Models._


	implicit val timeout = Timeout(1 seconds)

	val collection = db[BSONCollection]("users")
	println("5")

  
	def findUserByEmail(email: String)= {
	    collection.find(BSONDocument("email" -> email)).one[UserModel]
	    
	}


	def findUserById(id: BSONObjectID) = {
		println(id)
	    collection.find(BSONDocument("_id" -> id)).one[UserModel] 
	    
	}


	private def getID(id: String) = 
		BSONDocument("_id" -> BSONObjectID(id))

  	private def insertFollower(id: String) = 
  		BSONDocument("$push" -> BSONDocument("followers" -> BSONObjectID(id)))

  	private def insertFollowing(id: String) = 
  		BSONDocument("$push" -> BSONDocument("followings" -> BSONObjectID(id)))

  	private def removeFollower(id: String) = 
  		BSONDocument("$pop" -> BSONDocument("followers" -> BSONObjectID(id)))

  	private def removeFollowing(id: String) = 
  		BSONDocument("$pop" -> BSONDocument("followings" -> BSONObjectID(id)))
  	
  	def follow(aid:String,bid:String)={
  		findUserById(BSONObjectID(aid)).map{userAOption=>
			userAOption match {
		    	case Some(userA) => 
		    		if(userA.followings.contains(BSONObjectID(bid))){
		    			StatusResponse("error")
		    		}
		    		else {
		    			collection.update(getID(bid), insertFollower(aid), upsert = true)
    					collection.update(getID(aid), insertFollowing(bid), upsert = true)
    					StatusResponse("ok") 
		    		}
		    	case _  => StatusResponse("error")
		    }
  		}
  		
  	}

  	def unfollow(aid:String,bid:String)={
  		findUserById(BSONObjectID(aid)).map{userAOption=>
			userAOption match {
		    	case Some(userA) => 
		    		if(userA.followings.contains(BSONObjectID(bid))){
		    			collection.update(getID(bid), removeFollower(aid))
    					collection.update(getID(aid), removeFollowing(bid)) 
    					StatusResponse("ok") 
		    		}
		    		else {
		    			StatusResponse("error")
		    		}
		    	case _  => StatusResponse("error")
		    }
  		}
  		
  	}
  	

  	def authenticateLogin(user:UserLogin) ={
  		findUserByEmail(user.email).map{m =>
  			m match {
		    	case Some(m) =>  
		    		if(m.password==user.password){
			    		UserLoginFound(m.id.stringify)
			    	}else {
			    		UserLoginNotFound
			    	}
		    	case _  => println("nothing");UserLoginNotFound
		    }
  		}
  	}


  	def isExist(id:BSONObjectID)={
  		findUserById(id).map{m=>
			m match {
		    	case Some(m) => println("exist user");true
		    	case _  => println("not exist user");false
		    }
  		}

  		/*
  		val fut=findUserById(id)
  		Await.result(fut, timeout.duration) match {
	    	case Some(m) => println("exist user");true
	    	case _  => println("not exist user");false
	    }
	    */
  	}
	

	def authenticateSignUp(email:String) ={
  		findUserByEmail(email).map{ m =>
  			m match {
  				case Some(m) => false
  				case _ => true
  			}
  			
  		}
  		/*
  		val fut=findUserByEmail(email)
  		Await.result(fut, timeout.duration) match {
	    	case Some(m) => false
	    	case _  => true
	    }
	    */
  	}
	
	def newUser(userModel :UserModel )= {

		authenticateSignUp(userModel.email).map{m=>
            m match {
				case true => collection.insert(userModel).map(_ => UserCreated(userModel.id.stringify))
				case _ =>Future{UserAlreadyExists}
			}           
        }.flatMap(m => m)
		/*
		if (authenticateSignUp(userModel.email)){
			collection.insert(userModel).map(_ => UserCreated(userModel.id.stringify))
		}else{
			Future{UserAlreadyExists}
		}
		*/
		
	}


  
}