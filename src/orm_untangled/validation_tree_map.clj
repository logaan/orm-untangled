(ns orm_untangled.validation_tree_map)

(defmulti validate
  (fn [validation data]
    (:type validation)))

(defn child-errors [{:keys [left right]} data]
  (let [errors (map #(validate % data) [left right])]
    (reduce concat errors)))

(defn join [left right]
  {:type ::join :left left :right right})

(defmethod validate ::join [validation data]
  (child-errors validation data))

(defn chain [left right]
  {:type ::chain :left left :right right})

(defmethod validate ::chain [validation data]
  (let [errors (child-errors validation data)
        error  (first (filter not-empty errors))]
    (if (nil? error) [] [error])))

(defn simple [selector predicate message]
  {:type ::simple :selector selector :predicate predicate :message message})

(defmethod validate ::simple [{:keys [selector predicate message]} data]
  (if (predicate (get-in data (flatten selector)))
    [message] []))

