%Suppose we add the noun “men” (which is plural) and the verb “shoot”. Then we
%would want a DCG which says that “The men shoot” is ok, ‘The man shoots” is ok,
%The men shoots” is not ok, and “The man shoot” is not ok. Change the DCG so
%that it correctly handles these sentences. Use an extra argument to cope with the
%singular/plural distinction.


s --> np, vp.

np --> det,n.

vp --> v,np.
vp --> v.

det --> [the].
det --> [a].

n --> [woman].
n --> [man].

v --> [shoots].
