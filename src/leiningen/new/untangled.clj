(ns leiningen.new.untangled
  (:require
    [leiningen.new.tmpl :as tmpl]
    [leiningen.new.templates :as template]
    [leiningen.core.main :as main]
    [stencil.parser :as p]
    [stencil.core :as s]
    [clojure.edn]
    [clojure.string :refer [replace-first]]
    [clojure.java.io :as io]))

(defn render-files [opts data]
  (let [render #((template/renderer "untangled") % data)
        tmpl (fn [files-map] (mapv (fn [[from to]] (vector to (render from))) files-map))]
    (->> tmpl/files
         (mapv (fn [[opt-name opt-files]]
                 (if (some-> opt-files meta :always)
                   (tmpl opt-files)
                   (if-not (get opts opt-name) []
                     (tmpl opt-files)))))
         (apply concat)
         (apply template/->files data))))

(defn make-data [project-name opts]
  (merge
    ;; VARS
    {:name          project-name
     :sanitized     (template/name-to-path project-name)}
    ;; LAMBDAS
    {:when-devcards   #(when     (:devcards opts) %)
     :when-server     #(when     (:server   opts) %)
     :when-not-server #(when-not (:server opts)   %)}))

(defn untangled
  [project-name & args]
  (let [opts (reduce (fn [opts arg]
                       (as-> (replace-first arg #"^:" "") arg
                         (keyword arg)
                         (assoc opts arg true)))
                     {} args)]
    (main/info "Generating an untangled project.")
    (main/info (str "With opts: " opts))
    (render-files opts (make-data project-name opts))))
