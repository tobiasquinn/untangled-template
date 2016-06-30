(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "Hello World, from Untangled!"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]

                 [navis/untangled-client "0.5.3"]
                 [navis/untangled-spec "0.3.8" :scope "test"]

                 {{#when-server}}
                 [com.taoensso/timbre "4.3.1"]
                 [navis/untangled-server "0.6.0"]
                 {{/when-server}}
                 {{#when-datomic}}
                 [navis/untangled-datomic "0.4.10"]
                 [com.datomic/datomic-free "0.9.5344"]
                 {{/when-datomic}}]

  :plugins [[com.jakemccrary/lein-test-refresh "0.15.0"]]

  :repositories [["releases" {:url "https://artifacts.buehner-fry.com/artifactory/release"
                              :update :always}]]

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
                                   :output-to            "resources/public/js/compiled/client.js"
                                   :output-dir           "resources/public/js/compiled/dev"
                                   :asset-path           "js/compiled/dev"
                                   :source-map-timestamp true
                                   :optimizations        :none}}

                       {:id           "production"
                        :source-paths ["src/client"]
                        :compiler     {:main          {{name}}.main
                                       :output-to     "resources/public/js/compiled/main.js"
                                       :output-dir    "resources/public/js/compiled/prod"
                                       :asset-path    "js/compiled/prod"
                                       :optimizations :none}}

                       {:id           "test"
                        :source-paths ["specs/client" "src/client"]
                        :figwheel     true
                        :compiler     {:main                 {{name}}.spec-main
                                       :output-to            "resources/public/js/specs/specs.js"
                                       :output-dir           "resources/public/js/compiled/specs"
                                       :asset-path           "js/compiled/specs"
                                       :optimizations        :none}}

                       {{#when-devcards}}
                       {:id           "cards"
                        :figwheel     {:devcards true}
                        :source-paths ["src/client" "src/cards"]
                        :compiler     {:main                 {{name}}.cards
                                       :output-to            "resources/public/js/compiled/cards.js"
                                       :output-dir           "resources/public/js/compiled/cards"
                                       :asset-path           "js/compiled/cards"
                                       :optimizations        :none
                                       :source-map-timestamp true}}
                       {{/when-devcards}}]}

  :profiles {:dev {:source-paths ["dev/client" "dev/server" "src/client" "src/server"]
                   :dependencies [[binaryage/devtools "0.6.1"]

                                  [com.cemerick/piggieback "0.2.1"]
                                  [figwheel-sidecar "0.5.3-1" :exclusions [joda-time clj-time {{#when-server}}ring/ring-core{{/when-server}}]]
                                  [org.clojure/tools.nrepl "0.2.12"]

                                  {{#when-devcards}}
                                  [devcards "0.2.1" :exclusions [org.omcljs/om]]
                                  {{/when-devcards}}]
                   :repl-options {:init-ns user
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})
