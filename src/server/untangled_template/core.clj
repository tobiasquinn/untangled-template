(ns untangled-template.core
  (:require
    [com.stuartsierra.component :as component]
    [untangled.server.core :as c]
    [untangled.server.impl.components.config :refer [load-config]]
    [taoensso.timbre :as timbre]
    [untangled-template.system :as sys])
  (:gen-class))

(def config-path "/usr/local/etc/untangled_template.edn")

(defn -main [& args]
  (let [system (sys/make-system config-path)]
    (component/start system)))
