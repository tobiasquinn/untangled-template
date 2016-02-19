(ns {{name}}.core
  (:require [om.next :as om]
            [untangled.client.core :as uc]

            {{name}}.state.mutations ;; DO NOT DELETE, loads {{name}}'s mutations
            [{{name}}.initial-state :refer [initial-state]]
            ))

(defonce app
  (atom (uc/new-untangled-client
          :initial-state initial-state
          :started-callback (fn [{:keys [reconciler]}]
                              ;;TODO: initial load of data
                              ))))
