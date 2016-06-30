(ns {{name}}.ui.root
  (:require
    [untangled.client.mutations :as mut]
    [om.dom :as dom]
    [om.next :as om :refer-macros [defui]]))

(defui ^:once Root
  static om/IQuery
  (query [this] [:ui/react-key])

  Object
  (render [this]
    (let [{:keys [ui/react-key] :or {ui/react-key "ROOT"}} (om/props this)]
      (dom/div #js {:key react-key}
               "Hello World!"))))
