s --> [].
s --> a,s,b.
a --> [a].
b --> [b].

s2 --> [].
s2 --> a, s, b, b.

prop --> [p].
prop --> [q].
prop --> [r].

prop --> not,prop.
prop --> lparen,prop,and,prop,rparen.
prop --> lparen,prop,or,prop,rparen.
prop --> lparen,prop,implies,prop,rparen.

not --> [not].

and --> [and].

or --> [or].

implies --> [implies].

lparen --> ['('].

rparen --> [')'].

 
