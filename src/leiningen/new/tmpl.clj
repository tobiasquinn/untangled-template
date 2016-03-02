(ns leiningen.new.tmpl
  (:require
    [leiningen.new.templates :as template]
    [clj-time.local :as l]))

(def files
  {:server          {"resources/config/defaults.edn"     "resources/config/defaults.edn"
                     "resources/config/dev.edn"          "resources/config/dev.edn"
                     "src/server/system.clj"             "src/server/{{sanitized}}/system.clj"
                     "src/server/core.clj"               "src/server/{{sanitized}}/core.clj"
                     "src/server/api/read.clj"           "src/server/{{sanitized}}/api/read.clj"
                     "src/server/api/mutations.clj"      "src/server/{{sanitized}}/api/mutations.clj"}

   :devcards        {"src/cards/cards.cljs"              "src/cards/{{sanitized}}/cards.cljs"
                     "src/cards/intro.cljs"              "src/cards/{{sanitized}}/intro.cljs"
                     "resources/cards.html"              "resources/public/cards.html"}

   :datomic         {"src/server/migrations/sample.clj"  "src/server/{{sanitized}}/migrations/{{sanitized}}_{{basic-date}}.clj"
                     "src/server/seed_data.clj"          "src/server/{{sanitized}}/{{sanitized}}_data.clj"}

   :client ^:always {"resources/index.html"              "resources/public/index.html"
                     "src/client/core.cljs"              "src/client/{{sanitized}}/core.cljs"
                     "src/client/main.cljs"              "src/client/{{sanitized}}/main.cljs"
                     "src/client/initial_state.cljs"     "src/client/{{sanitized}}/initial_state.cljs"
                     "src/client/state/mutations.cljs"   "src/client/{{sanitized}}/state/mutations.cljs"
                     "src/client/ui/root.cljs"           "src/client/{{sanitized}}/ui/root.cljs"}

   :spec ^:always   {"resources/test.html"               "resources/public/test.html"
                     "specs/client/spec_main.cljs"       "specs/client/{{sanitized}}/spec_main.cljs"
                     "specs/client/tests_to_run.cljs"    "specs/client/{{sanitized}}/tests_to_run.cljs"
                     "specs/client/sample_spec.cljs"     "specs/client/{{sanitized}}/sample_spec.cljs"}

   :dev ^:always    {"dev/server/user.clj"               "dev/server/user.clj"
                     "dev/client/cljs/user.cljs"         "dev/client/cljs/user.cljs"}

   :root ^:always   {"project.clj"                       "project.clj"
                     "README.md"                         "README.md"
                     "script/figwheel.clj"               "script/figwheel.clj"}})

(defn make-data [project-name opts]
  {:name      project-name
   :sanitized (template/name-to-path project-name)
   :basic-date (l/format-local-time (l/local-now) :basic-date)})

(def all (->> files (filter (comp not :always meta second)) (map first) set))

(def opts
  {:all all
   :datomic #{:datomic :server}})
