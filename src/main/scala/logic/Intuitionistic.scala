package logic

type Imp[P,Q] = P => Q
type Neg[P] = [A] => P => A
type Forall[T, P[X<:T]] = [X<:T] => Unit => P[X]

type ->[P, Q] = Imp[P,Q]
type ~[P] = Neg[P]
type ∀[T,P[X<:T]] = Forall[T,P]

def basic() : Unit =
  println("Hello")

  val trans_imp:[A,B,C] => A -> B => Imp[B,C] => Imp[A,C] =
    [A,B,C] => (p1:Imp[A,B]) => (p2:Imp[B,C]) =>
      (pA:A) => p2(p1(pA))

  val contra:[A,P] => P => (~[P] => A) =
    [A,P] => (p:P) => (np: ~[P]) =>
      np[A](p)

  val contra2:[A,P] => P => (~[P] => A) =
    [A,P] => (p:P) => (np: ~[P]) =>
      val what=np[Int](p)
      println(p)
      np[A](p)

  def ident[X<:Object] : X => X = (x:X) => java.util.function.Function.identity()(x)
  val funky = [X<:Object,P<:Object,Q<:Object] => (f:X => P) => (g:(X => P) => Q) => g((x:X) => ident(f(x)))

  val selfie : [X<:AnyRef] => (AnyRef => X) => X = [X] => (f:AnyRef => X) => f(f)

  inspect(trans_imp)
  inspect(contra)
  inspect(funky)
  inspect(selfie)
