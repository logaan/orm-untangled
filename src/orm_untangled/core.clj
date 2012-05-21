(ns orm-untangled.core)

(defrecord Person [name age sex])

(def logan
  (map->Person {:name "Logan" :age 24 :sex :male}))

logan
