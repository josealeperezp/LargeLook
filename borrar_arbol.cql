MATCH (a)<-[rels*]-(t {id : 'f4e811c1-c1a4-4d54-882d-9f64a642be5c'})
FOREACH(r in rels | DELETE r)
DELETE t,a
