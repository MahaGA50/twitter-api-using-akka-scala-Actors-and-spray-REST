package com.twitter_package

import akka.actor._
import akka.util.Timeout
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.duration._
import scala.language.postfixOps


import scala.concurrent.Future
import scala.util.{ Failure, Success }
import scala.concurrent.ExecutionContext.Implicits.global

import com.twitter_package.models.Models._
import com.twitter_package.models.TweetModel
import com.twitter_package.models.TweetModel._


class AkkaJsonParsing extends HttpServiceActor with RestApi {

  def receive = runRoute(routes)
}

trait RestApi extends HttpService with ActorLogging { actor: Actor =>

  implicit val timeout = Timeout(10 seconds)

  var Tweetes = Vector[Tweet]()
  var users = Vector[User]()

  val db= new DatabaseDriver

  def routes: Route =

      pathPrefix("users") {

        path("signup") {
          post {
            entity(as[User]) { user => requestContext =>
              val responder = createResponder(requestContext)
              
              db.signUp(user).onComplete {
                case Failure(e) => throw e
                case Success(m) => println("signup is "+m.getClass + " "+ m.toString());responder ! m
                    
              }
            }
          }
        }~
        path("login") {
          post  {
            entity(as[UserLogin]) { userlogin => requestContext =>
              val responder = createResponder(requestContext)
              db.login(userlogin).onComplete {
                case Failure(e) => throw e
                case Success(m) => responder ! m
                    
              }

            }
          }
        }~
        path(Segment / "home"){ id =>
         get {  requestContext =>
          val responder = createResponder(requestContext)
          db.getMyTweets(id.toString()).onComplete{
                case Failure(e) => throw e;responder ! TweetNotCreated
                case Success(m) => println("home is "+m.getClass + " "+ m.toString());
                responder ! m.map{ tweet =>toTweetResponse(tweet) }
            }
        
          }
        }~
        path(Segment / "tweets") { id =>
          get { requestContext =>
            val responder = createResponder(requestContext)
            db.getMyTweets(id.toString()).onComplete{
                case Failure(e) => throw e;responder ! TweetNotCreated
                case Success(m) => println("mytweets is "+m.getClass + " "+ m.toString());
                responder ! m.map{tweet => toTweetResponse(tweet)}
            }
          }
        }~
        path(Segment / "follow" / Segment) { (userid1,userid2)=>
          get { requestContext =>
            val responder = createResponder(requestContext)
            //db.follow(userid1.toString(),userid2.toString())
            
            db.follow(userid1.toString(),userid2.toString()).onComplete{
                  case Failure(e) => throw e
                  case Success(m) => println("follow is "+m.getClass + " "+ m.toString());
                  responder ! m
            }
            
          }
        }~
        path(Segment / "unfollow" / Segment) { (userid1,userid2)=>
          get { requestContext =>
            val responder = createResponder(requestContext)
            db.unfollow(userid1.toString(),userid2.toString()).onComplete{
                  case Failure(e) => throw e
                  case Success(m) => println("unfollow is "+m.getClass + " "+ m.toString());
                  responder ! m
            }
            
            /*
            db.unfollow(userid1.toString(),userid2.toString()).onComplete{
                  case Failure(e) => throw e
                  case Success(m) => println("unfollow is "+m.getClass + " "+ m.toString());
                  responder ! m
            }
            */
          
          }
        }
    }~
    pathPrefix("tweets") {
      pathEnd {
        post {
          entity(as[Tweet]) { tweet => requestContext =>
            val responder = createResponder(requestContext)
            db.addTweet(tweet).onComplete{
              case Failure(e) => throw e;responder ! TweetNotCreated
              case Success(m) => println("mytweets is "+m.getClass + " "+ m.toString());responder ! m
            }
          }
        }
      }~
      path(Segment) { id =>
           get { requestContext =>
          val responder = createResponder(requestContext)
          db.getTweet(id.toString()).onComplete{
              case Failure(e) => throw e
              case Success(m) => m match {
                case Some(m)=> responder ! toTweetResponse(m)
                case _ => responder ! TweetNotFound
              }
            }
        }
      }~
      path(Segment) { id =>
        delete { requestContext =>
          val responder = createResponder(requestContext)
          db.deleteTweet(id.toString()).onComplete{
              case Failure(e) => throw e
              case Success(m) => responder ! m 
            }
        }
      }  
        
    }
        
        

  private def createResponder(requestContext:RequestContext) = {
    context.actorOf(Props(new Responder(requestContext)))
  }
 

}



class Responder(requestContext:RequestContext) extends Actor with ActorLogging {
   

  
  def receive = {

    case userCreated:UserCreated =>
      requestContext.complete(StatusCodes.Created,userCreated)
      killYourself

    case userLoginFound:UserLoginFound =>
      requestContext.complete(StatusCodes.Created,userLoginFound)
      killYourself

    case UserAlreadyExists =>
      requestContext.complete(StatusCodes.Created,"user already exists")
      killYourself


    case oneuser: User =>
      requestContext.complete(StatusCodes.Created, oneuser)
      killYourself

    case UserLoginNotFound =>
      requestContext.complete(StatusCodes.Created,"wrong login data")
      killYourself

    case status:StatusResponse =>
      requestContext.complete(StatusCodes.Created,status)
      killYourself

    case tweetCreated:TweetCreated =>
      requestContext.complete(StatusCodes.Created,tweetCreated)
      killYourself

    case TweetNotCreated =>
      requestContext.complete(StatusCodes.Created,"author doesn't exist")
      killYourself



    case TweetNotFound =>
      requestContext.complete(StatusCodes.Created,"tweet not found")
      killYourself

    case tweet: TweetResponse =>
      requestContext.complete(StatusCodes.Created, tweet)
      killYourself

    case tweets :List[TweetResponse] => 
      requestContext.complete(StatusCodes.Created, tweets)
      killYourself

    case TweetDeleted =>
      requestContext.complete(StatusCodes.Created,"tweet deleted")
      killYourself

    /*
    case m:Future[UserCreated] => 
      m.onComplete{
        case Failure(e) => throw e
        case Success(m) => 
          requestContext.complete(StatusCodes.Created,m)
          killYourself
      }
    */   

    case a => 
      println("error is "+a.getClass + " "+ a.toString())
      requestContext.complete(StatusCodes.Created,"error")
      killYourself
  }

  private def killYourself = self ! PoisonPill
  
}

