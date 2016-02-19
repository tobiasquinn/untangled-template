(ns {{name}}.api.read
  (:require
    ;[untangled.datomic.protocols :as udb]
    [taoensso.timbre :as timbre]))

(timbre/info "Loading API definitions for {{name}}.api.read")

(defn api-read [{:keys [query] :as env} disp-key params]
  ;(let [connection (udb/get-connection survey-database)])
  (case disp-key
    :hello-world {:value 42}
    (throw (ex-info "Invalid request" {:query query :key disp-key}))))
