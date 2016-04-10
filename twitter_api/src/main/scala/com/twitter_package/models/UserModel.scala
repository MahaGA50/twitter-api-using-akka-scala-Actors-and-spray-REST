package com.twitter_package.models

import com.twitter_package.models.Models.User
import reactivemongo.bson.{BSONDocumentWriter, BSONDocument, BSONDocumentReader, BSONObjectID}

case class UserModel(id: BSONObjectID = BSONObjectID.generate,
                      followers :List[BSONObjectID] =List[BSONObjectID](),
                      followings :List[BSONObjectID] =List[BSONObjectID](),
                      username: String, 
                      email: String,
                      password :String)

object UserModel {

  implicit def toUserModel(user: User) = UserModel(username = user.username, email = user.email,password=user.password)

  
  implicit object UserModelBSONReader extends BSONDocumentReader[UserModel] {
    
    def read(doc: BSONDocument): UserModel = 
      UserModel(
        id = doc.getAs[BSONObjectID]("_id").get,
        followers= doc.getAs[List[BSONObjectID]]("followers").get,
        followings= doc.getAs[List[BSONObjectID]]("followings").get,
        username = doc.getAs[String]("username").get,
        email = doc.getAs[String]("email").get,
        password = doc.getAs[String]("password").get
        
      )
  }
  
  implicit object UserModelBSONWriter extends BSONDocumentWriter[UserModel] {
    def write(userModel: UserModel): BSONDocument =
      BSONDocument(
        "_id" -> userModel.id,
        "followers" -> userModel.followers,
        "followings" -> userModel.followings,
        "username" -> userModel.username,
        "email" -> userModel.email,
        "password" -> userModel.password
        
      )
  }
}