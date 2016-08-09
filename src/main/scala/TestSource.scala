package test.source

import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import akka.stream.{Attributes, Outlet, SourceShape}
import test.actions._
import test.models.Models.Something

class TestSource[T <: Something](implicit action: DoSomething[T]) extends GraphStage[SourceShape[T]] {
  val out: Outlet[T] = Outlet("TestSource")
  override val shape: SourceShape[T] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
    new GraphStageLogic(shape) {
      var counter: Int = 0

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          val item = fetchItem
          counter += 1

          push(out, item)
        }

        private def fetchItem(): T = {
          action.exec(counter)
        }
      })
    }
  }
}