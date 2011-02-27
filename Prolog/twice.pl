twice([], []).
twice([H|T1], [H,H| T2]) :- twice(T1, T2).
