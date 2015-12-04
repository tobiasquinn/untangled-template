(ns {{name}}.handler
  (:require [com.stuartsierra.component :as component]
            [bidi.bidi :refer [match-route]]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.resource :refer [wrap-resource]]
            ))

(def routes
  ["" {"/"    :index
       "/api" :api}])

(defn index [req]
  (-> "index.html"
      (resource-response {:root "public"})
      (assoc :headers {"Content-Type" "text/html"})))

(defn gen-res [data & [status]]
  {:status  (or status 200)
   :headers {"Content-Type" "application/edn"}
   :body    (pr-str data)})

(defn api [req]
  (try
    (println "API Received " req)
    (gen-res {:res :api, :req req})
    (catch Exception e
      (println "API error:" e)
      (gen-res {:cthulhu :angry}))))

(defn make-route-handler [routes]
  (fn [req]
    (let [match (match-route routes (:uri req))]
      (case (:handler match)
        :index (index req)
        :api   (api req)
        req))))

(defn make-handler [routes]
  (-> routes
      make-route-handler
      (wrap-resource "public")))

(defrecord Handler [value]
  component/Lifecycle
  (start [this]
    (println "Creating web server handler.")
    (assoc this :value (make-handler routes)))
  (stop [this]
    (println "Tearing down web server handler.")
    (assoc this :value nil)))

(defn new-handler []
  (map->Handler {}))
