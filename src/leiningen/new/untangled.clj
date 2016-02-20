(ns leiningen.new.untangled
  (:require
    [leiningen.new.tmpl :as tmpl]
    [leiningen.new.templates :as template]
    [leiningen.core.main :as main]
    [clojure.set :as set]
    [stencil.parser :as p]
    [stencil.core :as s]
    [clojure.string :refer [replace-first]]
    [clojure.java.io :as io]))

(defn render-files [opts data]
  (let [render #((template/renderer "untangled") % data)
        tmpl (fn [files-map]
               (mapv (fn [[from to]]
                       (vector to (render from)))
                     files-map))]
    (->> tmpl/files
         (mapv (fn [[opt-name opt-files]]
                 (if (some-> opt-files meta :always)
                   (tmpl opt-files)
                   (if-not (get opts opt-name) []
                     (tmpl opt-files)))))
         (apply concat)
         (apply template/->files data))))

(defn parse-args [args]
  (letfn [(parse [args]
                 (reduce #(->> %2 read-string (conj %1)) #{} args))
          (validate [opts args]
                    (let [diff (set/difference args (set (keys opts)))]
                      (assert (empty? diff) (str "invalid args: " diff)))
                    args)
          (expand [arg->opts args]
                  (->> args
                       (mapcat #(if-let [xs (arg->opts %)] xs [%]))
                       set
                       (reduce #(assoc %1 %2 true) {})))]
    (->> (parse args)
         (validate tmpl/opts)
         (expand tmpl/opts))))

(defn untangled
  [project-name & args]
  (let [opts (parse-args args)]
    (main/info "Generating an untangled project.")
    (main/info (str "With opts: " opts))
    (render-files opts (tmpl/make-data project-name opts))
    (main/info "Done! see README for info")))
