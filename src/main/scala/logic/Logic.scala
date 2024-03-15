package logic

type Imp[P,Q] = P => Q
type Neg[P] = [A,F] => A => P => F
type Forall[A, P] = A => P

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

val injection: [M<:Nat,N<:Nat] => Equals[Succ[M],Succ[N]] => Equals[M,N] =
  [M<:Nat,N<:Nat] => (p:Equals[Succ[M],Succ[N]]) =>
    new Axiom("injection") with Equals[M,N] {}


def main(args: Array[String]): Unit =
  println("Yes")

  val p:Equals[Zero,Zero] = injection(refl[Succ[Zero]])
  val q:Equals[Zero,Zero]=>Equals[Zero,Succ[Zero]] = arrowAxiom.axiom

  println(p)
  println(q(p))