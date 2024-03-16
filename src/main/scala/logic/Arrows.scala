package logic

case class Arrows(val arrows:List[Arrows]=List()):
  override def toString:String =
    def f : List[Arrows] => String =
      case Nil => "()"
      case Arrows(Nil)::t => f(t) + " <- ()"
      case h::t => f(t)+" <- ("+h+")"
    f(arrows.reverse)

  def addHead:Arrows =
    Arrows(Arrows()::arrows)

def merge(a: Arrows, b: Arrows): Arrows =
  def f : (List[Arrows],List[Arrows]) => List[Arrows] =
    case (Nil, Nil) => Nil
    case (a,Nil) => a
    case (Nil,b) => b
    case (a::ta,b::tb) => merge(a,b)::f(ta,tb)
  Arrows(f(a.arrows,b.arrows))
