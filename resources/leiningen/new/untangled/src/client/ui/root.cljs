(ns {{name}}.ui.root
  (:require
    [untangled.client.mutations :as mut]
    [om.dom :as dom]
    [om.next :as om :refer-macros [defui]]
    ))

(defui ^:once Root
  static om/IQuery
  (query [this] [:react-key])

  Object
  (render [this]
    (let [{:keys [react-key] :or {react-key "ROOT"}} (om/props this)]
      (dom/div #js {:key react-key}
               "Hello World!"))))
