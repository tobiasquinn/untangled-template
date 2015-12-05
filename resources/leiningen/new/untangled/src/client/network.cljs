(ns {{name}}.network
  (:import [goog.net XhrIo EventType])
  (:require goog.object
            [om.next :as om]
            [goog.events :as events]
            [cljs.reader :as reader]

            [{{name}}.utils :refer [spy]]))

(defonce valid-data-callback (atom nil))

(defmulti server-error-handler :type)

(defmethod server-error-handler :default [error]
  (println "Unexpected error from server:" error))

(defmethod server-error-handler :network [error]
  (println "Network error:" error))

(defn response-ok [e]
  (println "RESPONSE OK")
  (this-as this
           (let [{:keys [query-response error] :as response}
                 (reader/read-string (.getResponseText this))]
             (when error
               (server-error-handler error))
             (when (and query-response @valid-data-callback)
               (@valid-data-callback query-response)))))

(defn request-complete [e]
  (println "REQUEST COMPLETE CALLED.")
  (this-as this
           (if (.isSuccess this)
             (println "RESULT SUCCEEDED. ")
             (server-error-handler {:type :network}))))

(defonce xhr-io (let [rv (XhrIo.)]
                  (events/listen rv (.-SUCCESS EventType) response-ok)
                  (events/listen rv (.-COMPLETE EventType) request-complete)
                  rv))

(defn transit-post [url]
  (fn [edn cb]
    (let [headers #js {"Content-Type" "application/edn"}]
      (reset! valid-data-callback cb)
      (.send xhr-io (str "http://localhost:3000" url) "POST" (pr-str edn) headers))))

(def post (transit-post "/api"))

(defn server-send
  [queries cb]
  (let [{:keys [query rewrite]} (om/process-roots (:server queries))]
    (post query (comp cb rewrite))))
