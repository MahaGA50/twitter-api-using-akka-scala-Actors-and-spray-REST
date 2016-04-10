package com.twitter_package.models


object Models {

  import spray.json._

  case class User(username: String="defaultname", email:String, password:String)

  case class UserCreated(id: String)
  
  case object UserAlreadyExists

  
  case class UserLogin(email: String, password: String)

  case class UserLoginFound(id: String)

  case object UserLoginNotFound
  
  
  case class Tweet(authorId: String, body:String)

  case class TweetResponse(id:String,authorId:String,body:String)

  case class TweetCreated(id: String)

  case object TweetNotCreated
  
  case object TweetDeleted
  
  case object TweetNotFound

  case class StatusResponse(status:String)
  
  object StatusResponse extends DefaultJsonProtocol {
    implicit val format  = jsonFormat1(StatusResponse.apply)
  }
  

  object Tweet extends DefaultJsonProtocol {
    implicit val format  = jsonFormat2(Tweet.apply)
  }

  object TweetCreated extends DefaultJsonProtocol {
    implicit val format  = jsonFormat1(TweetCreated.apply)
  }

  object TweetResponse extends DefaultJsonProtocol {
    implicit val format  = jsonFormat3(TweetResponse.apply)
  }

  object UserLogin extends DefaultJsonProtocol {
    implicit val format  = jsonFormat2(UserLogin.apply)
  }

  object UserLoginFound extends DefaultJsonProtocol {
    implicit val format  = jsonFormat1(UserLoginFound.apply)
  }
  

  object User extends DefaultJsonProtocol {

    implicit val format = jsonFormat3(User.apply)
  }

  object UserCreated extends DefaultJsonProtocol {

    implicit val format = jsonFormat1(UserCreated.apply)
  }
      


}
