(ns untangled-template.system
  (:require
    [com.stuartsierra.component :as component]
    [untangled-template.api.mutations :as m]
    [untangled-template.api.read :as r]
    [ring.middleware.content-type :refer [wrap-content-type]]
    [ring.middleware.gzip :refer [wrap-gzip]]
    [ring.middleware.not-modified :refer [wrap-not-modified]]
    [ring.middleware.params :refer [wrap-params]]
    [ring.middleware.resource :refer [wrap-resource]]
    [untangled.server.core :as core]
    [untangled.server.impl.middleware :as middleware]
    [clojure.java.io :as io]
    [taoensso.timbre :as timbre]))

; This is both a server module AND hooks into the Om parser for the incoming /api read/mutate requests. The
; modular server support lets you chain as many of these together as you want, allowing you to define
; reusable Om server components.
(defrecord ApiHandler []
  core/Module
  (system-key [this] ::api)
  (components [this] {})
  core/APIHandler
  (api-read [this] r/api-read)
  (api-mutate [this] m/apimutate))

(defn build-api-handler [& [deps]]
  "`deps`: Vector of keys passed as arguments to be
  included as dependecies in `env`."
  (component/using
    (map->ApiHandler {}) deps))

(defn MIDDLEWARE [handler component]
  ((get component :middleware) handler))

(defn not-found [req]
  {:status  404
   :headers {"Content-Type" "text/plain"}
   :body    "Resource not found."})

(defrecord CustomMiddleware [middleware api-handler]
  component/Lifecycle
  (stop [this] (dissoc this :middleware))
  (start [this]
    (assoc this :middleware
                (-> not-found
                  (MIDDLEWARE api-handler)
                  ;; TRANSIT
                  middleware/wrap-transit-params
                  middleware/wrap-transit-response
                  ;; RESOURCES
                  (wrap-resource "public")
                  ;; HELPERS
                  wrap-content-type
                  wrap-not-modified
                  wrap-params
                  wrap-gzip))))

; IMPORTANT: You want to inject the built-in API handler (which is where modular API handlers get chained)
(defn build-middleware []
  (component/using
    (map->CustomMiddleware {})
    {:api-handler ::core/api-handler}))

(defrecord Sample [stuff]
  component/Lifecycle
  (start [this] (assoc this :stuff {:data 42}))
  (stop [this] this))

(defn make-system [config-path]
  (core/untangled-system
    {:components {:config      (core/new-config config-path) ; you MUST use config if you use our server
                  ::middleware (build-middleware) ; complete Ring stack
                  :sample-component (map->Sample {})
                  :web-server  (core/make-web-server ::middleware)} ; must provide the component key to your middleware
     ; Modules are composable into the Om API handler (can have their own read/mutate) and
     ; are joined together in a chain. Any components you declare as deps will appear in the parser env.
     :modules    [(build-api-handler [:sample-component])]}))
