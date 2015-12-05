(ns {{name}}.parser
  (:require [om.next :as om]))

(defn local-read [{:keys [state query ast] :as env} k params]
  (println :LOCAL-READ query k params)
  (->> (let [sub-state (get @state k)]
         (cond
           (not query)
           sub-state

           (om/ident? (:key ast))
           (get-in @state (:key ast))

           :else
           (om/db->tree query sub-state @state)))
       (assoc {} :value)))

(defn new-read-entry-point [default-local-read remote-read-map]
  (fn [{:keys [state target] :as env} k params]
    (cond
      {{#when-server}}
      (contains? remote-read-map target) ((get remote-read-map target) env k params)
      {{/when-server}}
      :else (default-local-read env k params))))

{{#when-server}}
(defn remote-read [{:keys [target ast]} k params]
  (println :REMOTE-READ target k ast)
  {target ast})

{{/when-server}}
(def read-entry-point (new-read-entry-point local-read {{#when-server}}{:server remote-read}{{/when-server}}{{#when-not-server}}{}{{/when-not-server}}))
