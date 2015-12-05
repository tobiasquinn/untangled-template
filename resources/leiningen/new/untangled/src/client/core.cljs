(ns {{name}}.core
  (:require [goog.dom :as gdom]
            [om.next :as om]

            [{{name}}.ui :as ui]
            [{{name}}.parser :as p]
            {{#when-server}}
            [{{name}}.network :as net]
            {{/when-server}}
            ))

(enable-console-print!)

(def initial-state {:hello "World!"})

(def parser (om/parser {:read p/read-entry-point}))

(def reconciler (om/reconciler {:state initial-state
                                :parser parser
                                {{#when-server}}
                                :send net/server-send
                                :remotes [:server]
                                {{/when-server}}
                                }))

(om/add-root! reconciler ui/Root (gdom/getElement "app"))
