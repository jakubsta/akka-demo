package test.actions

import test.models.Models.{SomeA, SomeB}


trait DoSomething[T] {
  def exec(a: Int): T
}

object DoSomething {
  implicit object DoSomethingWithA extends DoSomething[SomeA] {
    override def exec(a: Int): SomeA = {
      SomeA("SomeA", a, true, 3.0)
    }
  }

  implicit object DoSomethingWithB extends DoSomething[SomeB] {
    override def exec(a: Int): SomeB = {
      SomeB("SomeB", 100 - a, false, 'x')
    }
  }
}

