(defproject com.evanjbowling/base "0.1.0-SNAPSHOT"
  :description "arbitrary-precision conversion of decimal representations"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins      [[lein-cljfmt "0.6.4"]]
  :dependencies [[org.clojure/clojure            "1.8.0"]
                 [org.clojure/math.numeric-tower "0.0.4"]]
  :aliases {"lint" ["cljfmt"]}
  :target-path "target/%s"
  :repl-options {:init-ns com.evanjbowling.base}
  :global-vars {*warn-on-reflection* true}
  :profiles {:uberjar {}})
