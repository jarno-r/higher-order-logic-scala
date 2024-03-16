package logic

type Imp[P,Q] = P => Q
type Neg[P] = [F] => ([A] => A => P) => F
type Forall[T, P[X<:T]] = [X<:T] => Unit => P[X]
type ->[P, Q] = P => Q
type ~[P] = [F] => ([A] => A => P) => F

def basic() : Unit =
  println("Hello")

  val trans_imp:[A,B,C] => A -> B => Imp[B,C] => Imp[A,C] =
    [A,B,C] => (p1:Imp[A,B]) => (p2:Imp[B,C]) =>
      (pA:A) => p2(p1(pA))

  val contra:[A,P] => P => ~[P] => A =
    [A,P] => (p:P) => (np:Neg[P]) =>
      np ([X] => (_:X) => p)

  type X
  val imp:X -> X = (p:X) => p

  def ident[X<:Object] : X => X = (x:X) => java.util.function.Function.identity()(x)
  val funky = [X<:Object,P<:Object,Q<:Object] => (f:X => P) => (g:(X => P) => Q) => g((x:X) => ident(f(x)))

  inspect(trans_imp)
  inspect(contra)
  inspect(funky)
