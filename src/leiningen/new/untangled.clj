(ns leiningen.new.untangled
  (:require [leiningen.new.templates :as template]
            [leiningen.core.main :as main]
            [stencil.parser :as p]
            [stencil.core :as s]
            [clojure.string :refer [replace-first]]))

(defn spy
  ([x] (spy :spy x))
  ([tag x] (println tag x) x))

(defn parse-args [args]
  (reduce (fn [opts arg]
            (as-> (replace-first arg #"^:" "") arg
              (keyword arg)
              (assoc opts arg true)))
          {} args))

(defn server-files [opts]
  (if-not (:server opts) []
    [["src/server/{{sanitized}}/system.clj"  "src/server/system.clj"]
     ["src/server/{{sanitized}}/config.clj"  "src/server/config.clj"]
     ["src/server/{{sanitized}}/handler.clj" "src/server/handler.clj"]
     ["src/server/{{sanitized}}/server.clj"  "src/server/server.clj"]]))

(defn client-files [opts]
  [["resources/public/index.html"          "resources/index.html"]
   ["src/client/{{sanitized}}/core.cljs"   "src/client/core.cljs"]
   ["src/client/{{sanitized}}/ui.cljs"     "src/client/ui.cljs"]
   ["src/client/{{sanitized}}/parser.cljs" "src/client/parser.cljs"]])

(defn devcard-files [opts]
  (if-not (:devcards opts) []
    [["src/cards/{{sanitized}}/cards.cljs" "src/cards/cards.cljs"]
     ["src/cards/{{sanitized}}/intro.cljs" "src/cards/intro.cljs"]
     ["resources/public/cards.html"        "resources/cards.html"]]))

(defn dev-files [opts]
  [["src/dev/user.clj" "src/dev/user.clj"]])

(defn root-files [opts]
  [["project.clj" "project.clj"]
   ["README.md"   "README.md"]])

(def files
  [root-files dev-files devcard-files
   client-files server-files])

(defn render-files [files data]
  (let [render #((template/renderer "untangled") % data)]
    (->> files
         (mapv #(update % 1 render))
         (apply template/->files data))))

(defn make-data [project-name opts]
  {:name          project-name
   :sanitized     (template/name-to-path project-name)
   :when-devcards #(when (:devcards opts) %)
   :when-server   #(when (:server   opts) %)
   })

(defn untangled
  "FIXME: write documentation"
  ([project-name & args]
   (let [opts (parse-args args)]
     (main/info "Generating an untangled project.")
     (main/info (str "With opts: " opts))
     (render-files (mapcat #(% opts) files)
                   (make-data project-name opts)))))
