(ns user
  (:require
    [figwheel-sidecar.repl-api :as ra]

    ;; repl utils
    [clojure.pprint :refer [pprint]]
    [clojure.stacktrace :refer [print-stack-trace]]
    [clojure.java.io :as io]

    {{#when-server}}
    [taoensso.timbre :as timbre]
    [clojure.tools.namespace.repl :refer [refresh set-refresh-dirs]]
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

(defonce system (atom nil))

(defn init [] (reset! system (sys/make-system)))

(defn start [] (swap! system component/start))
(defn stop [] (swap! system component/stop) (reset! system nil))

(defn go [] (init) (start))

(defn reset [] (stop) (refresh :after 'user/go))
{{/when-server}}
