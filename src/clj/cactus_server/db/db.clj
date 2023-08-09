(ns cactus-server.db.db
    (:require
      [monger.core :as mg]
      [monger.collection :as mc]
      [monger.operators :refer :all] 
      [monger.credentials :as mcr]
      [mount.core :refer [defstate]]
      [cactus-server.config :refer [env]]))

(let [conn (mg/connect)
      db   (mg/get-db conn "monger-test")
      oid  (ObjectId.)
      doc  {:first_name "John" :last_name "Lennon"}]
  (mc/insert db "documents" (merge doc {:_id oid})))

(defstate db*
  :start (-> env :database-url mg/connect-via-uri)
  :stop (-> db* :conn mg/disconnect))

(defstate db
  :start (:db db*))

(defn create-user [user]
  (mc/insert db "users" user))

(defn update-user [id first-name last-name email]
  (mc/update db "users" {:_id id}
             {$set {:first_name first-name
                    :last_name last-name
                    :email email}}))

(defn get-user [id]
  (mc/find-one-as-map db "users" {:_id id}))



;;Authentication

(let [admin-db   "admin"
      u    "username"
      p    (.toCharArray "password")
      cred (mcr/create u admin-db p)
      host "127.0.0.1"]
  (mg/connect-with-credentials host cred))

(let [conn (mg/connect)]
  (mg/disconnect conn))