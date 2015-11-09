(set-env!
  :dependencies '[[adzerk/boot-cljs          "1.7.48-6"]
                  [adzerk/boot-reload        "0.4.1"]
                  [compojure                 "1.4.0"]
                  [hoplon/boot-hoplon        "0.1.10"]
                  [hoplon/castra             "3.0.0-SNAPSHOT"]
                  [hoplon/hoplon             "6.0.0-alpha10"]
                  [korma                     "0.4.2"]
                  [org.xerial/sqlite-jdbc    "3.7.15-M1"]
                  [org.clojure/clojure       "1.7.0"]
                  [org.clojure/clojurescript "1.7.145"]
                  [pandeiro/boot-http        "0.7.0"]
                  [ring                      "1.4.0"]
                  [ring/ring-defaults        "0.1.5"]]
  :resource-paths #{"assets" "src/clj"}
  :source-paths   #{"src/cljs" "src/hl"})

(require
  '[adzerk.boot-cljs      :refer [cljs]]
  '[adzerk.boot-reload    :refer [reload]]
  '[hoplon.boot-hoplon    :refer [hoplon prerender]]
  '[pandeiro.boot-http    :refer [serve]]
  '[korma.core            :refer [exec-raw]]
  '[korma.db              :refer [defdb sqlite3]])

(deftask dev
  "Build users-crud for local development."
  []
  (comp
    (serve
      :port    8000
      :handler 'users-crud.handler/app
      :reload  true)
    (watch)
    (speak)
    (hoplon)
    (reload)
    (cljs)))

(deftask createdb
  "Create a database with some seeds for us to work"
  []
    (defdb crud (sqlite3 {:db "users.db"}))
    (exec-raw ["CREATE TABLE users (id INTEGER PRIMARY KEY, name VARCHAR(255))"] :keys)
    (exec-raw ["CREATE TABLE systems (id INTEGER PRIMARY KEY, name VARCHAR(255))"] :keys)
    (exec-raw ["CREATE TABLE attributions (id INTEGER PRIMARY KEY,
      systems_id INTEGER, users_id INTEGER )"] :keys)

    (exec-raw ["INSERT INTO users (name) VALUES
      ('Ariovaldo Steil'), ('Amanda Loring'), ('Andrew Neo')"] :keys)
    (exec-raw ["INSERT INTO systems (name) VALUES
      ('Active Directory'), ('Email'), ('SAP')"] :keys))

(deftask prod
  "Build users-crud for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)))

(deftask make-war
  "Build a war for deployment"
  []
  (comp (hoplon)
        (cljs :optimizations :advanced)
        (uber :as-jars true)
        (web :serve 'users-crud.handler/app)
        (war)))
