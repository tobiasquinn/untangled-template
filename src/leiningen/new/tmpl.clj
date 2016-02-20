(ns leiningen.new.tmpl
  (:require
    [leiningen.new.templates :as template]))

(defn make-data [project-name opts]
  {;;VARS
   :name      project-name
   :sanitized (template/name-to-path project-name)
   ;;LAMBDAS
   :when-devcards   #(when     (:devcards opts) %)
   :when-server     #(when     (:server   opts) %)
   :when-not-server #(when-not (:server opts)   %)})

(def opts
  {:server   nil
   :devcards nil
   :all      #{:server :devcards}})

(def files
  {:server          {"resources/config/defaults.edn"   "resources/config/defaults.edn"
                     "src/server/system.clj"           "src/server/{{sanitized}}/system.clj"
                     "src/server/core.clj"             "src/server/{{sanitized}}/core.clj"
                     "src/server/api/read.clj"         "src/server/{{sanitized}}/api/read.clj"
                     "src/server/api/mutations.clj"    "src/server/{{sanitized}}/api/mutations.clj"}

   :devcards        {"src/cards/cards.cljs"            "src/cards/{{sanitized}}/cards.cljs"
                     "src/cards/intro.cljs"            "src/cards/{{sanitized}}/intro.cljs"
                     "resources/cards.html"            "resources/public/cards.html"}

   :client ^:always {"resources/index.html"            "resources/public/index.html"
                     "src/client/core.cljs"            "src/client/{{sanitized}}/core.cljs"
                     "src/client/main.cljs"            "src/client/{{sanitized}}/main.cljs"
                     "src/client/initial_state.cljs"   "src/client/{{sanitized}}/initial_state.cljs"
                     "src/client/state/mutations.cljs" "src/client/{{sanitized}}/state/mutations.cljs"
                     "src/client/ui/root.cljs"         "src/client/{{sanitized}}/ui/root.cljs"}

   :spec ^:always   {"resources/test.html"             "resources/public/test.html"
                     "specs/client/spec_main.cljs"     "specs/client/{{sanitized}}/spec_main.cljs"
                     "specs/client/tests_to_run.cljs"  "specs/client/{{sanitized}}/tests_to_run.cljs"
                     "specs/client/sample_spec.cljs"   "specs/client/{{sanitized}}/sample_spec.cljs"}

   :dev ^:always    {"dev/server/user.clj"             "dev/server/user.clj"
                     "dev/client/cljs/user.cljs"       "dev/client/cljs/user.cljs"}

   :root ^:always   {"project.clj"                     "project.clj"
                     "README.md"                       "README.md"
                     "script/figwheel.clj"             "script/figwheel.clj"}})