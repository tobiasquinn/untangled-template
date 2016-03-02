(ns {{name}}.system
  (:require
    [untangled.server.core :as core]
    [untangled.datomic.core :refer [build-database]]
    [om.next.server :as om]
    [{{name}}.api.read :as r]
    [{{name}}.api.mutations :as mut] ;; DO NOT DELETE, loads mutations

    [taoensso.timbre :as timbre]))

;; IMPORTANT: Remember to load all multi-method namespaces to ensure all of the methods are defined in your parser!

(defn logging-mutate [env k params]
  ; NOTE: Params can be a security/pci concern, so don't log them here.
  ; TODO: Include user info from env in logs.
  (timbre/info "Mutation Request: " k)
  (mut/apimutate env k params))

(defn make-system []
  (let [config-path "/usr/local/etc/{{sanitized}}.edn"]
    (prn :make-system/config-path config-path)
    (core/make-untangled-server
      :config-path config-path
      :parser (om/parser {:read r/api-read :mutate logging-mutate})
      :parser-injections #{}
      :components {})))
