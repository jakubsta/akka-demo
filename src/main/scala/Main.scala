package test

import akka.actor.ActorSystem
import akka.stream.scaladsl.{Balance, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source}
import akka.stream.{ActorMaterializer, ClosedShape}
import test.models.Models.{SomeA, Something}
import test.source.TestSourceNonGeneric

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Main extends App {
  println("Start")

  implicit val system = ActorSystem("test")
  implicit val materializer = ActorMaterializer()

  val out = Sink.last[Something]
  val g: RunnableGraph[Future[Something]] = RunnableGraph.fromGraph(GraphDSL.create(out) { implicit builder => sink =>
    import GraphDSL.Implicits._
    val WORKER_COUNT = 10

    //    val in = Source.fromGraph(new TestSource[SomeA])
    val in = Source.fromGraph(new TestSourceNonGeneric)

    val balancer = builder.add(Balance[Something](WORKER_COUNT))
    val merge = builder.add(Merge[Something](WORKER_COUNT))

    val map = Flow[Something].map((s: Something) => s)
    val take = Flow[Something].take(10)


    in ~> balancer
    for (i <- 0 until WORKER_COUNT) {
      balancer.out(i) ~> map ~> merge.in(i)
    }
    merge ~> take ~> sink

    ClosedShape
  })

  g.run().onComplete {
    case Success(i) =>
      println(i)
      println("END!")
      system.terminate
    case Failure(r) =>
      println(r)
      system.terminate
  }
}
