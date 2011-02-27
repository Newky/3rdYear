connection(a, b).
connection(b, c).
connection(c, d).
connection(k,z).
connection(b,e).
connection(e,g).
connection(h,g).
connection(g,l).
connection(g,k).

path(X, Y, [connection(X, Y)]) :- connection(X, Y).
path(X, Y, [connection(X,Y) | ConnList]) :- connection(X, Z), path(Z, Y, ConnList).


swap(tree(leaf(X), leaf(Y)),tree(leaf(Y), leaf(X))).
swap(leaf(X), leaf(X)).
swap(tree(X, Y), tree(NewY, NewX)) :- swap(X, NewX), swap(Y, NewY). 

member(X, [X| _]).
member(X, [_ | T]) :- member(X, T).

nonMember(_, []).
nonMember(X, [H | T]) :- X \= H , nonMember(X, T).

no([], _).
no([X|L], List2) :- nonMember(X, List2),no(L, List2).

maxAcc([H|L], A, Max) :- H > A, maxAcc(L, H, Max).
maxAcc([H|L], A, Max) :- H =< A, maxAcc(L, A, Max).
maxAcc([], A, A).

max([H | L], Max) :- maxAcc([H | L], H, Max).

append([], L, L).
append([L1 | L1T], L2, [ L1| L3 ]) :- append(L1T, L2, L3).


reverse([], []).
reverse([H|T], Rev) :- reverse(T, Temp), append(Temp,[H],Rev).

reverse_acc([], Acc, Acc):
reverse_acc([H | T], Acc, Rev) :- reverse_acc(T, [Acc | H], Rev).
