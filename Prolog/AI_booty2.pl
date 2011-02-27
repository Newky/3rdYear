bottomUp(Goal, KB) :- logCon(KB, C), member(Goal, C).

logCon([KB|KBT],Res) :- fact_head_test(KB,[KB|KBT],C), append(C, [KB | KBT], EKB),logCon2(KBT,EKB, A), append(C, A, Res).

logCon2([CL| CLT], KB, Res) :- fact_head_test(CL, KB, C), logCon2(CLT, [KB|C], A), append(C, A, Res).
logCon2([], _, []).

fact_head_test(Test, KB2, C) :- fact_head(Test, KB2, C) ; append([], C, C).

fact_head([H|T],KB, [H]) :- member([H], KB) ; fact_tail(T, KB).

fact_tail([H|T], KB) :- member([H], KB) , fact_tail(T, KB).
fact_tail([], _).

member(X, [X|_]).
member(X, [_|L]) :- member(X, L).

append([], L, L).
append([H | T], L2, [[H] | L3]) :- append(T, L2, L3).

