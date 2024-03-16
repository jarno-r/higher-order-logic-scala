package logic

import scala.util.boundary
import scala.util.boundary.break

val MAX_DEPTH = 10

class MaxDepth extends Exception

enum Struct:
  case Stop

def inspect(p:AnyRef):Unit =
  println("Inspector Gadget")

  trait TestFunc {
    val parent:TestFunc
    val base:TestFunc
    val depth:Int
    val scanResult:List[TestFunc]
    var maxDepth:Int=0
    var arrows:Arrows=Arrows()

    def updateArrows(newArrows: Arrows): Unit =
      println(newArrows)
      arrows = merge(arrows, newArrows)
      if parent != null then
        parent.updateArrows(arrows.addHead)
  }

  def getArrowsFromGoal(ts:List[TestFunc]):Arrows=
    def f:List[TestFunc]=>List[Arrows] =
      case Nil => Nil
      case h::t => h.arrows::f(t)
    Arrows(List(Arrows(f(ts))))

  def makeTestFunction(spawner:TestFunc=null,scanRes:List[TestFunc]=List()): TestFunc =
    val tf = new Function[AnyRef,AnyRef] with TestFunc:
      val parent=spawner
      val base=if parent==null then this else parent.base
      val depth=if parent==null then 0 else parent.depth+1
      val scanResult=scanRes

      override def toString:String =
        s"${depth}/${maxDepth}(${hashCode})"

      def apply(x: AnyRef) : AnyRef =
        val s = scan(x)
        val a = getArrowsFromGoal(s.reverse.tail)
        //println("D "+depth+"/"+base.maxDepth+" | "+arrows+" | "+getArrowsFromGoal(s.reverse.tail)+" | "+a)
        arrows=merge(arrows,a)
        val tf = makeTestFunction(this,s)
        if parent!=null then
          parent.updateArrows(arrows.addHead)
        tf

    assert(tf.depth <= MAX_DEPTH)
    tf.base.maxDepth = Math.max(tf.base.maxDepth, tf.depth)
    tf

  def scan(p:AnyRef):List[TestFunc] =
    p match
      case f:TestFunc => List(f)
      case f:Function[AnyRef,AnyRef] =>
        val tf=makeTestFunction()
        val r=f(tf)
        tf::scan(r)

  val tf = scan(p)

  val alpha=AlphaBag[TestFunc]()

  def grab(f: TestFunc, hash:Boolean=false):String=
    s"${alpha(f)}"+(if hash then "(${f.hashCode})" else "")

  def printHead(tf:List[TestFunc],indent:Int=0):Unit =
    for f <- tf do
      print(" "*indent)
      print(s"${grab(f)}: ")
      println(f.arrows)

  def printBody(tf:TestFunc,indent:Int=0):Unit =
    def p(f:TestFunc):Unit =
      if f != null then
        if f.parent == null then
          print(s"${grab(f)}")
        else if f.scanResult.length<=1 then
          p(f.parent)
          if f.scanResult.head.depth==0 then
            print(s" ${grab(f.scanResult.head)}")
          else
            print(" (")
            printBody(f.scanResult.head,indent)
            print(")")
        else
          p(f.parent)
          println()
          printGoal(f.scanResult,indent+2)
    p(tf)

  def printGoal(tf:List[TestFunc],indent:Int=0):Unit =
    printHead(tf.reverse.tail.reverse,indent)
    println(" "*indent+"-----")
    print(" "*indent)
    printBody(tf.reverse.head,indent)
    println()

  printGoal(tf)
