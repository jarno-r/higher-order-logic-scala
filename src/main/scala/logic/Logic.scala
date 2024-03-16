package logic

/*
type Imp[P,Q] = P => Q
type Neg[P] = [A,F] => A => P => F
type Forall[T, P[X<:T]] = [X<:T] => Unit => P[X]
*/

trait Nat {}
trait Zero extends Nat {}
trait Succ[n<:Nat] extends Nat {}

case class Axiom(what: String) {}

trait Axiomatic[P]:
  def axiom : P

given equalAxiom[A,B]: Axiomatic[Equals[A,B]] =
  new Axiomatic[Equals[A,B]]:
    def axiom = new Axiom("axiom") with Equals[A,B]

given arrowAxiom[A,B](using axiomB:Axiomatic[B]): Axiomatic[A => B] =
  new Axiomatic[A => B]:
    def axiom = (a:A) => axiomB.axiom

trait Equals[A,B] {}

type lamppu = [P,Q] =>> (P => Q)

def negneg[P](using axiom: Axiomatic[P]): Neg[Neg[P]] => P =
  (p: Neg[Neg[P]]) =>
    axiom.axiom

def refl[N]: Equals[N,N] = new Axiom("refl") with Equals[N,N]
def symm[N,M]: Equals[N,M] => Equals[M,N] = (p:Equals[N,M]) => new Equals [M,N] {}

def trans[X,Y,Z]: Equals[X,Y] => Equals[Y,Z] => Equals[X,Z] =
  (p:Equals[X,Y]) => (q:Equals[Y,Z]) =>
    new Equals[X,Z] {}

val injection: [M<:Nat,N<:Nat] => Equals[Succ[M],Succ[N]] => Equals[M,N] =
  [M<:Nat,N<:Nat] => (p:Equals[Succ[M],Succ[N]]) =>
    new Axiom("injection") with Equals[M,N] {}

// S(n) != 0, probably needed.

def induction[P[N<:Nat],X<:Nat]: P[Zero] => ([M<:Nat] => P[M] => P[Succ[M]]) => P[X] = ???

type AllZero[X<:Nat] = Equals[X,Zero]

def main(args: Array[String]): Unit =
  println("Yes")

  basic()

  val p:Equals[Zero,Zero] = injection(refl[Succ[Zero]])
  val q:Equals[Zero,Zero]=>Equals[Zero,Succ[Zero]] = arrowAxiom.axiom

  println(p)
  println(q(p))