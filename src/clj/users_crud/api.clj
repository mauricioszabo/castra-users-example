(ns users-crud.api
  (:require [castra.core :refer [defrpc]]
            [korma.core :refer :all]
            [users-crud.db :refer :all]))

(defrpc get-users []
  (shuffle (select users (limit 50))))

(defrpc get-systems []
  (println (select systems))
  (select systems))

(defrpc edit-accounts [id]
  (first (select users (with systems) (where (= :id id)))))

(defrpc create-system [name]
  (if-not (first (select systems (where (= :name name))))
    (insert systems (values {:name name})))
  (get-systems))
