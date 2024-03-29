(ns guestbook.db.schema
  (:require [clojure.java.jdbc :as sql]
            [noir.io :as io])
)

(def db-store "site.db")


(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2"
              :subname (str (io/resource-path) db-store)
              :user "sa"
              :password ""
              :naming {:keys clojure.string/upper-case
                       :fields clojure.string/upper-case}})

; (def db-spec {:classname "org.h2.Driver"
;               :subprotocol "h2"
;               :subname (str (io/resource-path) db-store)
;               :user "sa"
;               :password ""
;               :make-pool? true
;               :naming {:keys clojure.string/lower-case
;                        :fields clojure.string/upper-case}}
; )

(defn initialized?
  "checks to see if the database schema is present"
  []
  (.exists (new java.io.File (str (io/resource-path) db-store ".mv.db"))))


(defn create-guestbook-table []
  (sql/db-do-commands
    db-spec
    (sql/create-table-ddl
      :guestbook
      [:id "INTEGER PRIMARY KEY AUTO_INCREMENT"]
      [:timestamp :timestamp]
      [:name "varchar(30)"]
      [:message "varchar(200)"]))
  (sql/db-do-prepared db-spec
      "CREATE INDEX timestamp_index ON guestbook (timestamp)"))

; (defn create-tables
; "create the database tables used by application"
; []
; (create-guestbook-table)
; )


;   (defn create-users-table
;     []
;     (sql/db-do-commands
;       db-spec
;       (sql/create-table-ddl
;         :users
;         [:id "varchar(20) PRIMARY KEY"]
;         [:first_name "varchar(30)"]
;         [:last_name "varchar(30)"]
;         [:email "varchar(30)"]
;         [:admin :boolean]
;         [:last_login :time]
;         [:is_active :boolean]
;         [:pass "varchar(100)"]))

;   )


(defn create-tables
  "creates the database tables used by the application"
  []
  (create-guestbook-table)
)