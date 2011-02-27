add(0, X, X).
add(s(X), Y, s(Z)) :- add(X, Y, Z).

noless(0, 0).
noless(0, s(X)).
noless(s(X), s(Y)) :- noless(X, Y).

noless2(X, Y) :- add(X, _, Y).

odd(s(0)).
odd(s(s(X))) :- odd(X).

even(0).
even(s(s(X))) :- even(X).
odd2(X) :- add(s(0), X, Y), even(Y).
