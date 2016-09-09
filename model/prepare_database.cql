create constraint on (n:User)
assert n.email is unique;

drop constraint on (n:User)
assert n.email is unique;