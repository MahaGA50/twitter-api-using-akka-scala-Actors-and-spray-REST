package com.twitter_package.models

import com.twitter_package.models.Models._
import reactivemongo.bson.{BSONDocumentWriter, BSONDocument, BSONDocumentReader, BSONObjectID}


case class TweetModel(id: BSONObjectID = BSONObjectID.generate,
                      authorId : BSONObjectID,
                      body :String)

object TweetModel {



  implicit def toTweetModel(tweet:Tweet) = TweetModel(authorId=BSONObjectID(tweet.authorId),body=tweet.body)
  implicit def toTweetResponse(tweetModel:TweetModel) = TweetResponse(id=tweetModel.id.stringify,authorId=tweetModel.authorId.stringify,body=tweetModel.body)

  implicit object TweetModelBSONReader extends BSONDocumentReader[TweetModel] {
    
    def read(doc: BSONDocument): TweetModel = 
      TweetModel(
        id = doc.getAs[BSONObjectID]("_id").get,
        authorId = doc.getAs[BSONObjectID]("authorId").get,
        body = doc.getAs[String]("body").get
        
      )
  }
  
  implicit object TweetModelBSONWriter extends BSONDocumentWriter[TweetModel] {
    def write(tweetModel: TweetModel): BSONDocument =
      BSONDocument(
        "_id" -> tweetModel.id,
        "authorId" -> tweetModel.authorId,
        "body" -> tweetModel.body
      )
  }

  
}