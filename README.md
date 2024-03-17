# Higher-Order Logic in Scala

## Introduction

The [Curry-Howard correspondence](https://en.wikipedia.org/wiki/Curry%E2%80%93Howard_correspondence) is a principle
that states that computer programs and logical proofs are fundamentally similar. More specifically, terms of a typed lambda
calculus can be used to represent proofs in predicate logic, while types represent propositions. A correctly typed expression
constitutes as a valid proof of the proposition expressed in the type. Since Scala is heavily based on lambda calculus, can we represent logic in Scala and have the Scala compiler check for correctness of the proofs? 

In an previous project [Propositional logic in Scala](https://github.com/jarno-r/propositional-logic-scala), I already showed
how simple propositional logic can be expressed using types Scala and how axioms provided as primitive elements in those types
can be used to construct proofs for propositional logic in a way that uses the Scala compiler as a proof-checker.

With polymorphic function types in Scala 3, it appears to be possible to express a much more powerful higher-order predicate logic directly in Scala.

## Construction of predicate logic in Scala

To construct propositions in higher-order predicate logic, we need the following things:

* A complete set of connectives for propositional logic
* A suitable quantifier
* Variables that can take values of any type, including propositions.

The implication is a natural choice due to its correspondence with functions. We can construct is as a type alias for a function:
````scala
type ->[P, Q] = P => Q
````
Then writing `X -> Y` is equivalent to writing `X => Y`, except `->` binds tighter.

Any expression that has the type `P -> Q` is a function `P => Q` that can be applied to any expression of type `P` creating an expression of type `Q`. If `P` and `Q` are propositions, applying `f:P -> Q` to `p:P` is equivalent to proving that `Q`, given `P -> Q` and `P`. 

Assuming that `P` is true, if we can prove `Q`, then `P -> Q` is true. This can be done by introducing a lambda expression of the form `(p:P) => <...>:Q`. This expression has the type `P -> Q`. 

Thus, without any additional constructs, we are able to perform operations equivalent to the implication introduction & elimination rules in natural deduction in Scala. Even the definition of the type alias `->` isn't strictly necessary, it is simply for notational convenience.

To complete the set of connectives, we additionally need negation. This can be defined as
````scala
type ~[P] = [A] => P => A
````
This can be understood to mean that given any arbitrary proposition `A`, if we can always prove `P -> A`, then `P` must not be true.
As with implication, this captures both the introduction & elimination rules for negation in natural deduction. The
introduction is straightforward, simply construct a function that has the type `~[P]`. The elimination rule in ND stipulates that `P` and `~P` proves anything. With `p: P` & `np: ~[P]`, we can write `np[<X>](p)`, where `<X>` stands for any type we like. 

The type variable `A` appears in the definition without any type constraints. Thus, `A` could be anything, not just a proposition.
What if we substitute `Int` for `A`? How do we 'prove' a proposition from an integer? What integer is `np[Int](p)`?
This shouldn't cause any real issues, since negation introduction & elimination are always paired and such substitutions cannot escape
an already contradictory context. I.e. while you could write `val i = np[Int](p)` inside a lambda, it would never be evaluated and would only participate in the type checking. For example, the `pritln(what)` below would never be executed, unless false evidence for `P` and `~P` are deliberately provided.
````scala
val contra:[A,P] => P => (~[P] => A) =
    [A,P] => (p:P) => (np: ~[P]) =>
        val what=np[Int](p)
        println(what)
        np[A](p)
````

Things get more interesting with quantifiers, since they introduce variables. Suppose that we wish to work with natural numbers. The proposition `∀x:Nat. P(x)` contains the proposition `P(x)` where `x` is a free variable in `P(x)`. It would seem natural to represent `P(x)` as a function from some type `Nat` to the type of propostitions, but we do not have a type for propositions. We cannot even have a supertype for propositions, because `P -> Q` cannot be coerced to be a subtype of something that we create. Since Scala does not have dependent types, regular variables cannot be elevated to the language types and type variables must be used instead.


