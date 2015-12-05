(ns {{name}}.utils)

(defn spy
  ([x] (spy :spy x))
  ([tag x] (println tag x) x))
