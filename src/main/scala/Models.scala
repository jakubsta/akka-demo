package test.models

object Models {
  trait Something {
    val f1: String
    val f2: Int
    val f3: Boolean
  }

  case class SomeA(f1: String, f2: Int, f3: Boolean, fA: Double) extends Something
  case class SomeB(f1: String, f2: Int, f3: Boolean, fB: Char) extends Something
}
