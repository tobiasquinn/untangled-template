(ns {{name}}.api.mutations
  (:require
    [clojure.walk :refer [prewalk]]
    [datomic.api :as d]
    [untangled.server.core :refer [arg-assertion transitive-join]]
    [untangled.datomic.core :refer [retract-datomic-entity resolve-ids]]
    [om.next.impl.parser :as omp]
    [om.tempid :as omt]
    [untangled.datomic.test-helpers :as seed]
    [untangled.datomic.protocols :as udb]
    [taoensso.timbre :as timbre])
  (:import (datomic.query EntityMap)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Mutation helpers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn upsert [connection client-data]
  "Given a db connection and a vector of data formatted for a datomic transaction, replaces client temp-ids
  with datomic temp-ids, and returns a map of client temp-ids to the real datomic ids created after the transaction
  is run.

  CANNOT BE RUN on a transaction that includes datomic entities, only plain data!
  "
  (let [client-temp-ids->datomic-tempids (atom {})
        transaction (prewalk (fn [id]
                               (when (instance? EntityMap id)
                                 (throw (ex-info "Entities cannot appear in upserts!" {:offender id})))
                               (if (omt/tempid? id)
                                 (let [tid (or (get @client-temp-ids->datomic-tempids id nil) (d/tempid :db.part/user))]
                                   (swap! client-temp-ids->datomic-tempids assoc id tid)
                                   tid)
                                 id)) client-data)
        resolved-ids->real-id (:tempids @(d/transact connection transaction))
        remaps (reduce
                 (fn [acc [cid dtmpid]]
                   (assoc acc cid (d/resolve-tempid (d/db connection) resolved-ids->real-id dtmpid)))
                 {}
                 @client-temp-ids->datomic-tempids)]
    {:tempids remaps}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Mutations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defmulti apimutate (fn [_ k _] k))

(comment
  (defmethod apimutate 'thing/create
    [{:keys [database]} mut-name {:keys [id value] :as params}]
    (arg-assertion id value)
    {:action (fn []
               (let [connection (udb/get-connection database)
                     tx-data [{:db/id id, :value value}]]
                 (timbre/info "Creating a thing")
                 (upsert connection tx-data)))}))
