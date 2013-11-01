(defproject proboscis "0.1.0-SNAPSHOT"
  :description "A utility for creating an ElasticSearch index and its mappings for Infosquito."
  :url "http://www.iplantcollaborative.org"
  :license {:name "BSD License"
            :url "http://iplantcollaborative.org/sites/default/files/iPLANT-LICENSE.txt"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.2.2"]
                 [cheshire "5.2.0"]
                 [clojurewerkz/elastisch "1.2.0"]
                 [slingshot "0.10.3"]]
  :resource-paths ["config"]
  :plugins [[org.iplantc/lein-iplant-cmdtar "0.1.0-SNAPSHOT"]]
  :repositories [["iplantCollaborative"
                  "http://projects.iplantcollaborative.org/archiva/repository/internal/"]]
  :aot [proboscis.core]
  :main proboscis.core)
