(ns {{name}}.config
  (:require [com.stuartsierra.component :as component]))

(defrecord Config [value]
  component/Lifecycle
  (start [this] this)
  (stop [this] this))

(defn new-config [value]
  (map->Config {:value value}))
