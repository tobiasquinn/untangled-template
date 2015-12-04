(ns user
  (:require [clojure.pprint :refer (pprint)]
            [clojure.stacktrace :refer (print-stack-trace)]
            [figwheel-sidecar.repl-api :as ra]

            {{#when-server}}
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.tools.nrepl.server :as nrepl]
            [com.stuartsierra.component :as component]
            [{{name}}.system :as sys]
            {{/when-server}}
            ))

{{#when-server}}
(defonce system (atom nil))

(defn init []
  (let [config {:port 3000}]
    (println "Using configuration:" config)
    (reset! system (sys/make-system config))))

(defn start [] (swap! system component/start))
(defn stop [] (swap! system component/stop))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (refresh :after 'user/go))

(defonce nrepl-server (atom nil))

(defn kill-nrepl []
  (swap! nrepl-server nrepl/stop-server))

(defn boot-nrepl [port]
  (when @nrepl-server (kill-nrepl))
  (reset! nrepl-server (nrepl/start-server :port port))
  (println "Started server nrepl at" port))
{{/when-server}}

(def figwheel-config
  {:figwheel-options {:server-port 3450}
   :build-ids        ["dev" {{#when-devcards}}"cards"{{/when-devcards}}]
   :all-builds       (figwheel-sidecar.config/get-project-builds)})

(defn start-dev []
  {{#when-server}}
  (boot-nrepl 7080)
  {{/when-server}}
  (ra/start-figwheel! figwheel-config)
  (ra/cljs-repl))
