(ns guestbook.routes.home
  (:require [compojure.core :refer :all]
            [guestbook.layout :as layout]
            [guestbook.util :as util]
            [guestbook.db.core :as db]
    )
)

; home page controller
; All we did here was update it to send some extra 
; parameters to the template, one of them being a list 
; of messages from the database.
(defn home-page [& [name message error]]
    (println :x "\n>>> running home-page")
    (println :m (db/get-messages))
    (layout/render "home.html"
        {:error error 
            :name name 
            :message message 
            :messages (db/get-messages)}
    )

)
; controller to post new messages
(defn save-message [name message]
    (cond

        (empty? name)
        (home-page name message "somebody forgot to leave name")
        
        (empty? message)
        (home-page name message "donet you have something to say?")

        :else
        (:do
            (db/save-message name message)
            (home-page)
        )
    )   
)



; (defn home-page []
;   (layout/render
;     "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

; routes
(defroutes home-routes
  (POST "/" [name message] (save-message name message))
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page)))
