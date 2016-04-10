package com.twitter_package

import reactivemongo.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

import play.api.libs.iteratee.Iteratee

import reactivemongo.bson._

import reactivemongo.api.QueryOpts
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.core.commands.Count

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

import scala.util.{ Failure, Success }

import scala.concurrent.duration.FiniteDuration

import com.twitter_package.database._
import com.twitter_package.models.UserModel
import com.twitter_package.models.TweetModel
import com.twitter_package.models.Models._


import scala.collection.mutable.ListBuffer

import scala.concurrent.duration._
import scala.concurrent.Await


class DatabaseDriver {

	val userDr=new UserDr
	val tweetDr=new TweetDr


	def signUp(userModel: UserModel) = userDr.newUser(userModel)

	def login(userLogin:UserLogin) =userDr.authenticateLogin(userLogin)

	def follow(aid:String,bid:String)= userDr.follow(aid,bid)

	def unfollow(aid:String,bid:String)= userDr.unfollow(aid,bid)


	def addTweet(tweetModel:TweetModel) ={
		userDr.isExist(tweetModel.authorId).map{m =>
			m match {
				case true => tweetDr.newTweet(tweetModel)
				case _ =>Future{TweetNotCreated}
			}  
		}.flatMap(m => m)
	} 

	def deleteTweet(id:String) = tweetDr.deleteTweet(BSONObjectID(id))

	def getTweet (id:String) = tweetDr.findTweetById(BSONObjectID(id))

	def getMyTweets(id:String)= tweetDr.getUserTweets(BSONObjectID(id))

	def getMyTimeLine(id:String)= for {
		  user <- userDr.getUserfindUserById(BSONObjectID(id))
		  followingArray <- Future {user(0).followings}
		  followingTweet <- tweetDr.getUsersTweets(followingArray)
		  userTweet <- tweetDr.getUserTweets(BSONObjectID(id))

		} yield followingTweet ++ userTweet
	



}