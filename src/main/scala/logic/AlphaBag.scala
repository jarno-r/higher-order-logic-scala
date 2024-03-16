package logic

import scala.collection.mutable.HashMap

object AlphaBag:
  def apply[T]():AlphaBag[T] =
    new AlphaBag[T]
class AlphaBag[T]:
  var ord:Char='a'-1
  val bag:HashMap[T,String]=new HashMap()

  private def next():String =
    ord=(ord+1).toChar
    String(Array(ord))

  def apply(k:T):String =
    bag.getOrElseUpdate(k,next())
