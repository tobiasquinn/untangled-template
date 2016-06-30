(ns user
  (:require
    [figwheel-sidecar.repl-api :as ra]

    ;; repl utils
    [clojure.pprint :refer [pprint]]
    [clojure.stacktrace :refer [print-stack-trace]]
    [clojure.java.io :as io]

    {{#when-datomic}}
    [datomic.api :as d]
    {{/when-datomic}}

    {{#when-server}}
    [taoensso.timbre :as timbre]
    [clojure.tools.namespace.repl :as tools-ns :refer [set-refresh-dirs]]
    [com.stuartsierra.component :as component]
    [{{name}}.system :as sys]{{/when-server}}))

;; ==================== FIGWHEEL ====================

(def figwheel-config
  {:figwheel-options {:css-dirs ["resources/public/css"]}
   :build-ids        ["dev" "test" {{#when-devcards}}"cards"{{/when-devcards}}]
   :all-builds       (figwheel-sidecar.repl/get-project-cljs-builds)})

(defn start-figwheel
  "Start Figwheel on the given builds, or defaults to build-ids in `figwheel-config`."
  ([]
   (let [props (System/getProperties)
         all-builds (->> figwheel-config :all-builds (mapv :id))]
     (start-figwheel (keys (select-keys props all-builds)))))
  ([build-ids]
   (let [default-build-ids (:build-ids figwheel-config)
         build-ids (if (empty? build-ids) default-build-ids build-ids)]
     (println "STARTING FIGWHEEL ON BUILDS: " build-ids)
     (ra/start-figwheel! (assoc figwheel-config :build-ids build-ids))
     (ra/cljs-repl))))
{{#when-server}}
;; ==================== SERVER ====================

(set-refresh-dirs "dev/server" "src/server" "specs/server")

(defn started? [sys]
  (-> sys :config :value))

(defonce system (atom nil))
(def cfg-paths {:dev "config/dev.edn"})

(defn refresh [& args]
  {:pre [(not @system)]}
  (apply tools-ns/refresh args))

(defn init [path]
  {:pre [(not (started? @system))
         (get cfg-paths path)]}
  (when-let [new-system (sys/make-system (get cfg-paths path))]
    (reset! system new-system)))

(defn start []
  {:pre [@system (not (started? @system))]}
  (swap! system component/start))

(defn stop []
  (when (started? @system)
    (swap! system component/stop))
  (reset! system nil))

(defn go
  ([] (go :dev))
  ([path] {:pre [(not @system) (not (started? @system))]}
   (init path)
   (start)))

(defn reset []
  (stop)
  (refresh :after 'user/go))

(defn engage [path & build-ids]
  (stop) (go path) (start-figwheel build-ids))
{{/when-server}}
