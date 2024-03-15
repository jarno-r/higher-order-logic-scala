package example

trait Key { type Value }

trait DB {
  def get(k: Key): Option[k.Value] // a dependent method
}

trait Kind {}

/*
object Forall {
  def apply(...)
}*/

//trait Prop { type D }
//type Forall[X] = (X => (p:Prop)) => 

//trait Neg[P] {}
//A => B

trait Prop {}

//trait Forall[A,B,F <: A => B] {}

//trait False {}

//val b : [A] => Prop => A = ???

type Neg[P] = [A] => P => A

val y = [C] => (x: C) => ??? //x * 2

//val p1: [A] => [B] => A => B => A = ??? /// 

def p1 = [A,B] => (a:A) => (b:B) => a

type Forall[A,P] = A => P



val negneg: [P] => Neg[Neg[P]] => P = [P] => (p:Neg[Neg[P]]) => ???

trait Nat {}
case class Zero() extends Nat {}
case class Succ(n:Nat) extends Nat {}

object Hello extends Greeting with App {
  println(greeting)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
