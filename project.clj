(defproject untangled/lein-template "0.3.2"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.7.0"]]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :eval-in-leiningen true

  :deploy-repositories [["releases" {:id            "central"
                                     :url           "https://artifacts.buehner-fry.com/artifactory/navis-maven-release"
                                     :snapshots     false
                                     :sign-releases false}]
                        ["snapshots" {:id            "snapshots"
                                      :url           "https://artifacts.buehner-fry.com/artifactory/navis-maven-snapshots"
                                      :sign-releases false}]])
