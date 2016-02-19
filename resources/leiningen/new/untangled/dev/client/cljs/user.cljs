(ns cljs.user
  (:require
    [devtools.core :as devtools]
    [untangled.client.core :as uc]
    [om.next :as om]

    [{{name}}.core :as core]
    [{{name}}.ui.root :as root]

    [cljs.pprint :refer [pprint]]))

(enable-console-print!)

(defonce tools-startup
  (do
    (devtools/enable-feature! :sanity-hints)
    (devtools/install!)))

(reset! core/app (uc/mount @core/app root/Root "app"))

(defn app-state [] @(:reconciler @core/app))

(defn log-app-state [& keywords]
  (pprint (let [app-state (app-state)]
            (if (= 0 (count keywords))
              app-state
              (select-keys app-state keywords)))))
