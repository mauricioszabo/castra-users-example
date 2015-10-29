(ns users-crud.api
  (:require [castra.core :refer [defrpc]]
            [korma.core :refer :all]
            [users-crud.db :refer :all]))

(defrpc get-users []
  (shuffle (select users (limit 50))))

(defrpc get-systems []
  (select systems))

(defrpc edit-accounts [id]
  (first (select users (with systems) (where (= :id id)))))

(defrpc create-system [name]
  (if-not (first (select systems (where (= :name name))))
    (insert systems (values {:name name})))
  (get-systems))

(defrpc add-attribution [user system-id]
  (if-not (some #(= (:id %) (Integer/parseInt system-id)) (:systems user))
    (insert attributions (values {:systems_id system-id, :users_id (:id user)})))
  (edit-accounts (:id user)))

(defrpc remove-attribution [user system-id]
  (delete attributions (where {:users_id (:id user), :systems_id system-id}))
  (edit-accounts (:id user)))
