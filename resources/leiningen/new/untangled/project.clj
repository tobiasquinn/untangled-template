(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "Hello Om, from Untangled!"

  :dependencies [[org.clojure/clojure "1.7.0" :scope "provided"]
                 [org.clojure/clojurescript "1.7.170" :scope "provided"]
                 [figwheel-sidecar "0.5.0-2" :scope "test"]
                 [org.omcljs/om "1.0.0-alpha24"]

                 {{#when-devcards}}
                 [devcards "0.2.1" :exclusions [org.omcljs/om]]
                 {{/when-devcards}}

                 {{#when-server}}
                 [org.clojure/tools.namespace "0.2.10"]
                 [ring/ring-core "1.4.0"]
                 [http-kit "2.1.18"]
                 [bidi "1.21.1"]
                 {{/when-server}}
                 ]

  :source-paths ["src/server"]

  :clean-targets ^{:protect false} ["target" "resources/public/js"]

  :figwheel {:build-ids ["dev"]}

  :cljsbuild {:builds [{:id "dev"
                        :figwheel true
                        :source-paths ["src/client"]
                        :compiler {:main                 {{name}}.core
                                   :asset-path           "js"
                                   :output-to            "resources/public/js/client.js"
                                   :output-dir           "resources/public/js"
                                   :recompile-dependents true
                                   :parallel-build       true
                                   :verbose              false}}
                       {{#when-devcards}}
                       {:id           "cards"
                        :figwheel     {:devcards true}
                        :source-paths ["src/client" "src/cards"]
                        :compiler     {:main                 {{name}}.cards
                                       :source-map-timestamp true
                                       :asset-path           "cards"
                                       :output-to            "resources/public/cards/cards.js"
                                       :output-dir           "resources/public/cards"
                                       :recompile-dependents true
                                       :parallel-build       true
                                       :verbose              false}}
                       {{/when-devcards}}
                       ]}

  :profiles {:dev {:source-paths ["src/dev"]
                   :repl-options {:init-ns user
                                  :port 7001}}}
  )
