(ns users-crud.db
  (:require [korma.core :refer :all]
            [korma.db :refer :all]))

(defdb crud (sqlite3 {:db "users.db"}))

(declare users attributions systems)

(defentity users
  (table :users)
  (database crud)
  (has-many attributions)
  (many-to-many systems :attributions))

(defentity systems
  (table :systems)
  (database crud)
  (has-many attributions)
  (many-to-many users :attributions))

(defentity attributions
  (table :attributions)
  (database crud)
  (belongs-to users)
  (belongs-to systems))
