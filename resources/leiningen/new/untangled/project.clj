(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "Hello Om, from Untangled!"

  :dependencies [[org.clojure/clojure "1.7.0" :scope "provided"]
                 [org.clojure/clojurescript "1.7.170" :scope "provided"]

                 [org.omcljs/om "1.0.0-alpha31-SNAPSHOT"]
                 [untangled-client "0.4.3-SNAPSHOT"]
                 [untangled-spec "0.3.3" :scope "test"]

                 [com.stuartsierra/component "0.3.0"]
                 [com.taoensso/timbre "4.1.4"]

                 {{#when-server}}
                 [navis/untangled-datomic "0.4.2-SNAPSHOT"]
                 [navis/untangled-server "0.4.3-SNAPSHOT"]
                 {{/when-server}}]

  :plugins [[com.jakemccrary/lein-test-refresh "0.13.0"]]

  :repositories [["releases"    "https://artifacts.buehner-fry.com/artifactory/navis-maven-release"]
                 ["snapshot"    "https://artifacts.buehner-fry.com/artifactory/navis-maven-snapshot"]
                 ["snapshot-2"  "https://artifacts.buehner-fry.com/artifactory/internal-snapshots"]
                 ["third-party" "https://artifacts.buehner-fry.com/artifactory/internal-3rdparty"]]

  :deploy-repositories [["releases" {:id            "central"
                                     :url           "https://artifacts.buehner-fry.com/artifactory/navis-maven-release"
                                     :snapshots     false
                                     :sign-releases false}]
                        ["snapshots" {:id            "snapshots"
                                      :url           "https://artifacts.buehner-fry.com/artifactory/navis-maven-snapshots"
                                      :sign-releases false}]]

  :test-refresh {:report untangled-spec.reporters.terminal/untangled-report}
  {{#when-server}}

  :source-paths ["src/server"]
  :jvm-opts ["-server" "-Xmx1024m" "-Xms512m" "-XX:-OmitStackTraceInFastThrow"]

  {{/when-server}}
  :test-paths ["specs" {{#when-server}}"specs/server" {{/when-server}}]
  :clean-targets ^{:protect false} [{{#when-server}}"target"{{/when-server}} "resources/public/js/compiled"]

  :figwheel {:css-dirs ["resources/public/css"]}

  :cljsbuild {:builds [{:id "dev"
                        :figwheel true
                        :source-paths ["src/client" "dev/client"]
                        :compiler {:main                 cljs.user
                                   :asset-path           "js/compiled/dev"
                                   :output-to            "resources/public/js/compiled/client.js"
                                   :output-dir           "resources/public/js/compiled/dev"
                                   :recompile-dependents true
                                   :verbose              false}}

                       {:id           "test"
                        :source-paths ["specs/client" "src/client"]
                        :figwheel     true
                        :compiler     {:main                 {{name}}.spec-main
                                       :output-to            "resources/public/js/specs/specs.js"
                                       :output-dir           "resources/public/js/compiled/specs"
                                       :asset-path           "js/compiled/specs"
                                       :recompile-dependents true
                                       :optimizations        :none}}

                       {{#when-devcards}}
                       {:id           "cards"
                        :figwheel     {:devcards true}
                        :source-paths ["src/client" "src/cards"]
                        :compiler     {:main                 {{name}}.cards
                                       :asset-path           "js/compiled/cards"
                                       :output-to            "resources/public/js/compiled/cards.js"
                                       :output-dir           "resources/public/js/compiled/cards"
                                       :optimizations        :none
                                       :recompile-dependents true
                                       :source-map-timestamp true
                                       :verbose              false}}
                       {{/when-devcards}}
                       ]}

  :profiles {:dev {:source-paths ["dev/client" "dev/server" "src/client" "src/server"]
                   :dependencies [
                                  [binaryage/devtools "0.5.2"]

                                  [com.cemerick/piggieback "0.2.1"]
                                  [figwheel-sidecar "0.5.0-3" :exclusions [ring/ring-core joda-time org.clojure/tools.reader]]
                                  [org.clojure/tools.nrepl "0.2.12"]

                                  {{#when-devcards}}
                                  [devcards "0.2.1" :exclusions [org.omcljs/om]]
                                  {{/when-devcards}}
                                  ]
                   :repl-options {:init-ns user
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]
                                  :port 7001}}})
