add(0, X, X).
add(s(X), Y, s(Z)) :- add(X, Y, Z).

multiply(0, _, 0).
multiply(s(X), Y, Z) :- multiply(X, Y, ZNew), add(Y, ZNew, Z).

exp(X, s(0), X).
exp(X,s(Y),Z) :- exp(X, Y, Znew), multiply(X,X,Znew2), add(Znew, Znew2, Z).
