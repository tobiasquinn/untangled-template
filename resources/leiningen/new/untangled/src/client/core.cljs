(ns {{name}}.core
  (:require [goog.dom :as gdom]
            [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]

            [{{name}}.ui :as ui]
            [{{name}}.parser :as p]
            ))

(enable-console-print!)

(def initial-state {:hello "World!"})

(def reconciler (om/reconciler {:state initial-state
                                :parser p/parser}))

(om/add-root! reconciler ui/Root (gdom/getElement "app"))
