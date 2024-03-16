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
  case Lambda(name:String, t:Struct)

def inspect(p:AnyRef):Unit =
  println("Inspector Gadget")

  def tryCalling(f:Function1[AnyRef,AnyRef], names:Int) : Struct =

    // Call with a0 -> ... -> an, until doesn't fail.
    boundary:
      for n <- 0 until MAX_DEPTH do
        def buildApp(hypos:List[Struct]):Struct =
          hypos match
            case Nil => Struct.Func("f"+names)
            case h::t => Struct.App(h,buildApp(t))

        var testFunc: List[Struct] => AnyRef = buildApp
        for i <- 0 until n do
          val tf=testFunc
          testFunc=(hypos:List[Struct]) => (a:AnyRef) =>
            val h=inspect(a,names+1)
            tf(hypos)

        try
          val g=testFunc(List())
          val ret = inspect(f(g),names+1)
          break(Struct.Lambda("g",ret))
        catch
          case _:MaxDepth =>
          case _:ClassCastException =>

      throw MaxDepth()

  def inspect(p:AnyRef,names:Int=0) : Struct =
    p match
      case f:Function1[AnyRef,AnyRef] =>
        println("A Function!")
        tryCalling(f,names)
      case s:Struct => s
      case what=>
        println("Something else: "+what)
        Struct.Stop

  val r = inspect(p)
  println(r)