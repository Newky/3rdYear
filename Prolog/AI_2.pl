#hdMin(C, S, [], [C|S]).
#hdMin(C, S, [H | T] , Result ) :-
	#( C < H, !, hdMin(C, [H | S], T, Result) , 

		#hdMin(H, [C|S], T Result). 

arc(N, M, Seed, Cost) :- M is N*Seed, Cost = 1.
arc(N, M, Seed, Cost) :- M is N*Seed + 1, Cost = 2.

is_goal(N, Target) :- 0 is N mod Target.

h(N, Hvalue, Target) :- is_goal(N, Target), !, Hvalue is 0
			;
			Hvalue is 1/N.

search([Node | FRest]) :- is_goal(Node).

search([Node | FRest]) :- setof(X,arc(Node, X), FNode), 
				add_to_frontier(FNode, FRest, FNew), 
				search(FNew).

arc(1,2).
arc(1,3).
arc(2,4).
arc(2,5).
arc(3,6).


