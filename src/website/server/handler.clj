(ns website.server.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [taoensso.timbre :as log]
            [clojure.data.json :as json]

            ;; Local data
            )
  )

(log/set-level! :trace)
(log/info "Logging initialised at info level")
(log/trace "Logging initialised at trace level")

(defroutes app-routes
  (route/resources "/")
  (route/not-found
    (json/write-str {:status "error" :message "Invalid endpoint"}))
  )

(def app
  (-> app-routes
      (wrap-defaults api-defaults)
      ring.middleware.session/wrap-session
      )
  )
