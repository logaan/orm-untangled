(ns orm-untangled.validation-types)

(defmulti validate
  "Validate functions take data and a validation. They return a seq of errors."
  #(type %2))

(defn validate-collection [data validations]
  (mapcat (partial validate data) validations))


(defn simple [& {:as description}]
  (with-meta description {:type ::simple}))

(defmethod validate ::simple [data {:keys [selector predicate message]}]
  (if (predicate (get-in data (flatten selector)))
    [message] []))


(defn join [& validations]
  (with-meta validations {:type ::join}))

(defmethod validate ::join [data validations]
  (validate-collection data validations))


(defn chain [& validations]
  (with-meta validations {:type ::chain}))

(defmethod validate ::chain [data validations]
  (take 1 (validate-collection data validations)))

