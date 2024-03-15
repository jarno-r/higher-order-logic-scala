package example

type Imp[P,Q] = P => Q
type Neg[P] = [A] => P => A
type Forall[A, P] = A => P

trait Nat {}
trait Zero extends Nat {}
trait Succ[n<:Nat] extends Nat {}

case class Axiom(what: String) {}

trait Equals[A,B] {}

val negneg: [P] => Neg[Neg[P]] => P =
  [P] => (p: Neg[Neg[P]]) =>
    ???

def refl[N]: Equals[N,N] = new Axiom("refl") with Equals[N,N]
def symm[N,M]: Equals[N,M] => Equals[M,N] = (p:Equals[N,M]) => new Equals [M,N] {}

val injection: [M<:Nat,N<:Nat] => Equals[Succ[M],Succ[N]] => Equals[M,N] =
  [M<:Nat,N<:Nat] => (p:Equals[Succ[M],Succ[N]]) =>

    new Axiom("injection") with Equals[M,N] {}


def main(args: Array[String]): Unit =
  println("Yes")

  val p:Equals[Zero,Zero] = injection(refl[Succ[Zero]])

  println(p)