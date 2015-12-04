(ns {{name}}.ui
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(defui Root
       static om/IQuery
       (query [this] [:hello])
       Object
       (render [this]
               (let [{:keys [hello]} (om/props this)]
                 (dom/div nil
                          "Hello " hello
                          {{lambda}}))))
