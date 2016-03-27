package com.twitter_package.database

import reactivemongo.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

import reactivemongo.bson._
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.api.QueryOpts
import reactivemongo.core.commands.Count
import reactivemongo.bson.{BSONDocumentWriter, BSONDocument, BSONDocumentReader, BSONObjectID}

import scala.concurrent.duration._
import akka.util.Timeout
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random
import scala.util.{ Failure, Success }

import scala.concurrent.Await



import com.twitter_package.models.TweetModel


class TweetDr extends MongoDr {

	import com.twitter_package.models.TweetModel._
	import com.twitter_package.models.Models._


	implicit val timeout = Timeout(1 seconds)

	val collection = db[BSONCollection]("tweets")
	println("5")


	
	def findTweetById(id: BSONObjectID) = {
	    collection.find(BSONDocument("_id" -> id)).one[TweetModel]
	    
	}

	def getUserTweets(id:BSONObjectID) ={  
		collection.find(BSONDocument("authorId" -> id)).
        cursor[TweetModel].
        collect[List](3)

	}

	def getUsersTweets(idList:List[BSONObjectID]) ={   ///// for timeline
		collection.find(BSONDocument("authorId" -> idList.head)).
        cursor[TweetModel].
        collect[List]()

	}

	def deleteTweet(id: BSONObjectID) = {
	    collection.remove(BSONDocument("_id" -> id),firstMatchOnly = true).map(_ => TweetDeleted)
	    
	}
	
	
	def newTweet(tweetModel :TweetModel )= {
			collection.insert(tweetModel).map(_ => TweetCreated(tweetModel.id.stringify))
		
	}

	
  
}