package logic

import scala.util.boundary
import scala.util.boundary.break

val MAX_DEPTH = 10

class MaxDepth extends Exception

enum Struct:
  case Stop
  case Arrow(a:Struct,b:Struct)
  case App(f:Struct, s:Struct)
  case Func(name:String)

case class Hyp()

case class Func(name:String)

type Dummy=(AnyRef,Hyp)
def inspect(p:AnyRef):Unit =
  println("Inspector Gadget")

  def tryCalling(f:Function1[AnyRef,AnyRef]) : Struct =

    // Call with a0 -> ... -> an, until doesn't fail.
    boundary:
      for n <- 0 until MAX_DEPTH do
        def buildApp(hypos:List[Struct]):Struct =
          hypos match
            case Nil => Struct.Func("f")
            case h::t => Struct.App(h,buildApp(t))

        var testFunc: List[Struct] => AnyRef = buildApp
        for i <- 0 until n do
          val tf=testFunc
          testFunc=(hypos:List[Struct]) => (a:AnyRef) =>
            val h=inspect(a)
            tf(hypos)

        try
          val g=testFunc(List())
          val ret = inspect(f(g))
          break(Struct.App(Struct.Func("g"), ret))
        catch
          case _:MaxDepth =>
          case _:ClassCastException =>

      throw MaxDepth()

  def inspect(p:AnyRef) : Struct =
    p match
      case f:Function1[AnyRef,AnyRef] =>
        println("A Function!")
        tryCalling(f)
      case what=>
        println("Something else: "+what)
        Struct.Stop

  inspect(p,List())