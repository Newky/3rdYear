# This prolog program needs to take a list of non neg numbers and return the max number, recursively

max([H | T], A, Max) :- 
	H > A, 
	max(T, H, Max).

max([H | T], A, Max) :- 
	H =< A, 
	max(T, A, Max).
max([], A, A).

increment(X,Y) :-
	Q is (X+1),
	Q =:= Y.

accRev([H|T],A,R) :- accRev(T,[H|A],R).
accRev([],A,A).

rev(L, R) :- accRev(L, [], R).

doubled(List) :- append(X, X, List).

append([], L, L).
append([H | T], L2, [H | L3]) :- append(T, L2, L3).

palindrome(List) :- rev(List, List).
