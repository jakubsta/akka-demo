package test.source

import akka.stream.{Attributes, Outlet, SourceShape}
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import test.actions.Actions.DoSomething
import test.actions.Actions.DoSomething.{DoSomethingWithA, DoSomethingWithB}
import test.models.Models.{SomeA, Something}

//class TestSource[T <: Something] extends GraphStage[SourceShape[T]] {
//  val out: Outlet[T] = Outlet("TestSource")
//  override val shape: SourceShape[T] = SourceShape(out)
//
//  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
//    new GraphStageLogic(shape) {
//      var counter: Int = 0
//
//      setHandler(out, new OutHandler {
//        override def onPull(): Unit = {
//          val item = fetchItem
//          counter += 1
//
//          push(out, item)
//        }
//
//        private def fetchItem(implicit action: DoSomething[T]): T = {
//          action.exec(counter)
//        }
//      })
//    }
//  }
//}

class TestSourceNonGeneric extends GraphStage[SourceShape[SomeA]] {
  val out: Outlet[SomeA] = Outlet("TestSource")
  override val shape: SourceShape[SomeA] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
    new GraphStageLogic(shape) {
      var counter: Int = 0

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          val item = fetchItem
          counter += 1

          push(out, item)
        }

        private def fetchItem(implicit action: DoSomething[SomeA]): SomeA = {
          action.exec(counter)
        }
      })
    }
  }
}