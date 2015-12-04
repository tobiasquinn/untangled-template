(ns {{name}}.parser
  (:require [om.next :as om]))

(defn local-read [{:keys [state query ast] :as env} k params]
  (->> (let [sub-state (get @state k)]
         (cond
           (not query)
           sub-state

           (om/ident? (:key ast))
           (get-in @state (:key ast))

           :else
           (om/db->tree query sub-state @state)))
       (assoc {} :value)))

(def parser (om/parser {:read local-read}))
