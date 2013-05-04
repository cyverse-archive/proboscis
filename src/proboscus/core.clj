(ns proboscus.core
  (:gen-class)
  (:use [clojure.tools.cli :only [cli]]
        [slingshot.slingshot :only [throw+ try+]])
  (:require [clojurewerkz.elastisch.rest :as esr]
            [clojurewerkz.elastisch.rest.index :as esi]))

(defn- to-int
  [s]
  (Integer. s))

(defn- parse-args
  [args]
  (cli args
       ["-h" "--host" "The host where ElasticSearch is running." :default "localhost"]
       ["-p" "--port" "The port that ElasticSearch is listening to." :parse-fn to-int
        :default 31338]
       ["-i" "--index" "The name of the index to create." :default "iplant"]
       ["-s" "--shards" "The total number of shards." :parse-fn to-int :default 1]
       ["-r" "--replicas" "The number of replicas of each shard." :parse-fn to-int
        :default 0]
       ["-?" "--[no-]help" "Display this help text."]))

(defmacro ^:private trap
  [[banner] & body]
  `(try+
    (do ~@body)
    (catch #(and (map? %) (contains? % :message)) {msg# :message}
      (binding [*out* *err*] (println msg#))
      (System/exit 1))
    (catch Object e#
      (binding [*out* *err*]
        (println "Unexpected Exception: " (str e#))
        (.printStackTrace e#))
      (System/exit 1))))

(defn- idx-settings
  [shards replicas]
  {:number_of_shards   shards
   :number_of_replicas replicas})

(defn- idx-mapping
  []
  (let [irods-entity {:type "string" :analyzer "irods_entity"}
        irods-path   {:type "string" :analyzer "irods_path"}
        not-analyzed {:type "string" :index "not_analyzed"}
        date         {:type "date"}]
    {:properties
     {:name        irods-entity
      :parent_path irods-path
      :creator     {:properties {:name not-analyzed
                                 :zone not-analyzed}}
      :create_date date
      :modify_date date
      :acl         {:properties {:name       not-analyzed
                                 :zone       not-analyzed
                                 :permission not-analyzed}}}}))

(defn- idx-mappings
  []
  {:file   (idx-mapping)
   :folder (idx-mapping)})

(defn- define-index
  [index shards replicas]
  (esi/create index :settings (idx-settings shards replicas) :mappings (idx-mappings)))

(defn- ensure-index-defined
  [{:keys [host port index shards replicas]}]
  (esr/connect! (str "http://" host ":" port))
  (when-not (esi/exists? index)
    (define-index index shards replicas)))

(defn -main
  [& args]
  (let [[opts args banner] (parse-args args)]
    (when (:help opts)
      (println banner)
      (System/exit 0))
    (trap [banner]
      (ensure-index-defined opts))))

;; Local Variables:
;; mode: clojure
;; eval: (define-clojure-indent (trap (quote defun)))
;; End:
