tran(eins,one).
tran(zwei,two).
tran(drei,three).
tran(vier,four).
tran(fuenf,five).
tran(sechs,six).
tran(sieben,seven).
tran(acht,eight).
tran(neun,nine).

#listtran(G,E) :- listtran(E, G).
listtran([], []).
listtran([G | X], [EH | ET]) :- tran(G, EH), listtran( X, ET).

twice([], []).
twice([Hin |In], [Hin, Hin |Out]) :- twice(In, Out).

combine([], [], []).
combine([Aone| A], [Bone | B], [Aone, Bone | C]) :- combine(A, B, C).

