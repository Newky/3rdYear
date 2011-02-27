%query for facts
lc([X|L],KB) :- append(L, KB, KB2),member([X|Xother], KB2), lcl(Xother, KB2).

lcl([], _).
lcl([H|L], KB) :- lc([H], KB), lcl(L, KB).

member(X, [X|_]).
member(X, [_|L]) :- member(X, L).

append([], L, L).
append([H | T], L2, [[H] | L3]) :- append(T, L2, L3).


