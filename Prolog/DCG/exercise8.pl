add(0, X, X).
add(s(X), Y, s(Z)) :- add(X, Y, Z).

noless(X, Y) :- X =< Y.

noless2(X, Y) :- add(X, -Y, Z), Z =< 0.

odd(s(0)).
odd(N) :- add(M, s(s(0)), N), odd(M). 

reverse([], []).
reverse([X|XT], Y) :- reverse(XT, Z), append(Z, [X], Y).

append([], L, L).
append([H | T], L2, [H | L3]) :- append(T, L2, L3).

succtobinary(X,A):-stobin(X,B),reverse(B, A).

stobin(0,[]).
stobin(s(X),B):- stobin(X,A) ,incremen(A,B).

incremen([],[1]).
incremen([0|A],[1|A]).
incremen([1|A],[0|B]):-incremen(A,B).

one-list([]).
one-list([1 | X]) :- one-list(X).

split([], [], []).
split(L, L1, L2) :- append(L1, L2, L), one-list(L2).

suffix(S, L) :- append(_, S, L).

s --> symbs(Count, a), symbs(Count2, b), symbs(Count*Count2, c).

symbs(end, _) --> [].
symbs(s(Count), S) --> [S], symbs(Count, S).
