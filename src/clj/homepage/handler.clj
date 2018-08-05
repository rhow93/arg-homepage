(ns homepage.handler
  (:require [compojure.core :refer [GET POST wrap-routes defroutes routes rfn]]
            [compojure.route :refer [resources]]
            [ring.util.response :refer [resource-response content-type]]
            [ring.middleware.reload :refer [wrap-reload]]
            [homepage.bot :refer [send-message]]
            [homepage.middleware :refer [wrap-front-middleware wrap-api-middleware]]))

(defn- send-front []
  (content-type (resource-response "index.html" {:root "public"}) "text/html; charset=utf-8"))

(defroutes static-resource-routes
  ;; re-frame application
  (GET "/" [] (send-front))
  (GET "/about" [] (send-front))
  (GET "/contact" [] (send-front))

  (resources "/"))

(defroutes api-routes
  (POST "/send-message" [:as {h :headers b :body}]
        (let [recaptcha-token (get h "g-recaptcha-response")]
          (do
            ;; TODO validate recaptcha-token

            ;; send message to bot
            ;; TODO parse result of bot and send back to frontend
            (send-message b)))))

(def handler
  (routes
   (wrap-routes api-routes wrap-api-middleware)
   (wrap-routes static-resource-routes wrap-front-middleware)))
