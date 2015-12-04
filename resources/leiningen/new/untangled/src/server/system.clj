(ns {{name}}.system
  (:require [com.stuartsierra.component :as component]

            [{{name}}.config  :refer [new-config]]
            [{{name}}.handler :refer [new-handler]]
            [{{name}}.server  :refer [new-server]]
            ))

(defn make-system [config]
  (component/system-map
    :config (new-config config)
    :handler (new-handler)
    :server (new-server)))
