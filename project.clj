(defproject com.evanjbowling/base "0.1.3-SNAPSHOT"
  :description "arbitrary-precision conversion of decimal representations"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins      [[lein-cljfmt    "0.6.4"]
                 [lein-cloverage "1.0.13"]
                 [lein-codox     "0.10.6"]]
  :dependencies [[org.clojure/clojure            "1.8.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]]
  :aliases {"lint" ["cljfmt"]}
  :target-path "target/%s"
  :codox {:output-path "doc"}
  :repl-options {:init-ns com.evanjbowling.base}
  :global-vars {*warn-on-reflection* true}
  :profiles 
    {:uberjar {}
     :dev 
       {:dependencies [[org.clojure/tools.namespace "0.2.11"]]}})
