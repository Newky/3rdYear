numeral(0).
numeral(s(X)) :- numeral(X).
numeral(p(X)) :- numeral(X).
numeral(X+Y) :- numeral(X), numeral(Y).
numeral(X-Y) :- numeral(X), numeral(Y).
numeral(-X) :- numeral(X).


add(0, s(p(X)), X).
add(0, X, X).
add(p(X), s(Y), Z) :- add(X,Y,Z).
add(s(X), p(Y), Z) :- add(X,Y,Z).
add(s(X), Y, s(Z)) :- add(X, Y, Z).
add(p(X), Y, p(Z)) :- add(X, Y, Z).
add2(-X,Y,Z) :- minus(X, Q), add2(Q, Y, Z), !.
add2(X,-Y,Z) :- minus(Y, Q), add2(X, Q, Z), !.
add2(X+P, s(Y+W), s(Z)) :- add(X,P,Q), add(Y,W,L), add(Q, L, Z), !.
add2(X+W, Y, Z) :- add(X,W,Q), add(Q,Y,Z), !.
add2(X, Y+W, Z) :- add(Y,W,Q), add(X,Q,Z), !.
add2(X-P, s(Y-W), s(Z)) :- subtract(X,P,Q), subtract(Y,W,L), add(Q, L, Z), !.
add2(X-W, Y, Z) :- subtract(X,W,Q), add(Q,Y,Z), !.
add2(X, Y-W, Z) :- subtract(Y,W,Q), add(X,Q,Z), !.
add2(X,Y,Z) :- add(X, Y, Z).

minus(0,0). 
minus(s(p(X)), Y) :- minus(X, Y).
minus(p(s(X)), Y) :- minus(X, Y).
minus(s(X), p(Y)) :- minus(X, Y), !.
minus(p(X), s(Y)) :- minus(X, Y), !.
minus(X-Y,Z) :- subtract(X,Y,Q),minus(Q, Z), !. 

subtract(X, Y, Z) :- minus(Y, Q), add2(X, Q, Z), !.
subtract(X, -Y, Z) :- add2(X, Y, Z), !.
