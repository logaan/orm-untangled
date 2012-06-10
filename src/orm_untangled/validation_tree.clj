(ns orm_untangled.validation_tree)

(defprotocol Validation
  (validate [self data]))

(defn child-errors [{:keys [left right]} data]
  (let [errors (map #(validate % data) [left right])]
    (reduce concat errors)))

(defrecord Join [left right]
  Validation
  (validate [self data]
    (child-errors self data)))

(defrecord Chain [left right]
  Validation
  (validate [self data]
    (let [errors (child-errors self data)
          error  (first (filter not-empty errors))]
      (if (nil? error) [] [error]))))

(defrecord Simple [selector predicate message]
  Validation
  (validate [{:keys [selector predicate message]} data]
    (if (predicate (get-in data (flatten selector)))
      [message] [])))

