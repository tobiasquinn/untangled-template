(ns {{name}}.server
  (:require [org.httpkit.server :refer [run-server]]
            [com.stuartsierra.component :as component])
  (:gen-class))

(defrecord Server [config handler server]
  component/Lifecycle
  (start [this]
    (println "Started web server")
    (try
      (assoc this :server
             (run-server (-> this :handler :value)
                         {:port (-> this :config :value :port)
                          :join? false}))
      (catch Exception e
        (println "Failed to start web server " e))))
  (stop [this]
    (when-let [server (:server this)]
      (server)
      (println "Web server stopped.")
      (assoc this :server nil))))

(defn new-server []
  (component/using
    (map->Server {})
    [:handler :config]))
