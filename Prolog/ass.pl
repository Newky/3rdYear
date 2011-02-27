numeral(0).
numeral(s(X)) :- numeral(X).
numeral(X+Y) :- numeral(X), numeral(Y).

add(0, X, X).
add(s(X), Y, s(Z)) :- add(X, Y, Z).

add2(X+W, Y, Z) :- add(X,W,Q), add(Q,Y,Z).
add2(X, Y+W, Z) :- add(Y,W,Q), add(X,Q,Z).
