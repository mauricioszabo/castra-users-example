(ns users-crud.api
  (:require [castra.core :refer [defrpc]]))

(defrpc get-state []
  {:random (rand-int 100)})
