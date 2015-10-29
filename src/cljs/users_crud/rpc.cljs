(ns users-crud.rpc
  (:require-macros
    [javelin.core :refer [defc defc=]])
  (:require
   [javelin.core]
   [castra.core :refer [mkremote]]))

(defc error nil)
(defc loading [])

(defc users [])
(def get-users (mkremote 'users-crud.api/get-users users error loading))

(defc user-to-edit nil)
(def edit-accounts (mkremote 'users-crud.api/edit-accounts user-to-edit error loading))
(defn edit-user [user-id]
  (edit-accounts user-id))
(defn remove-system [system-id]
  ((mkremote 'users-crud.api/remove-attribution user-to-edit error loading)
    @user-to-edit system-id))

(defn add-attribution [system-id]
  ((mkremote 'users-crud.api/add-attribution user-to-edit error loading)
    @user-to-edit system-id))

(defc systems [])
(defn get-systems [] ((mkremote 'users-crud.api/get-systems systems error loading)))

(defn create-system [name]
  ((mkremote 'users-crud.api/create-system systems error loading) name))

(defc= state
  {:editing user-to-edit
   :users users})

(defn init []
  (get-users))
