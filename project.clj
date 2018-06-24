(defproject homepage "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.10.238"]
                 [reagent "0.7.0"]
                 [re-frame "0.10.5"]
                 [secretary "1.2.3"]
                 [garden "1.3.5"]
                 [ns-tracker "0.3.1"]
                 [compojure "1.5.0"]
                 [soda-ash "0.79.1"]
                 [cljs-http "0.1.45"]
                 [hiccup "1.0.5"]
                 [venantius/accountant "0.2.4"]
                 [yogthos/config "0.8"]
                 [ring "1.4.0"]]

  :plugins [[lein-cljsbuild "1.1.7"]
            [lein-garden "0.2.8"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "resources/public/css"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler homepage.handler/dev-handler}

  :garden {:builds [{:id           "screen"
                     :source-paths ["src/clj"]
                     :stylesheet   homepage.css/screen
                     :compiler     {:output-to     "resources/public/css/screen.css"
                                    :pretty-print? true}}]}

  :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}

  :aliases {"dev" ["do" "clean"
                   ["pdo" ["figwheel" "dev"]
                    ["garden" "auto"]]]
            "build" ["with-profile" "+prod,-dev" "do"
                     ["clean"]
                     ["cljsbuild" "once" "min"]
                     ["garden" "once"]]}

  :profiles
  {:dev
   {:dependencies [[binaryage/devtools "0.9.10"]
                   [figwheel-sidecar "0.5.16"]
                   [cider/piggieback "0.3.5"]]

    :plugins      [[lein-figwheel "0.5.16"]
                   [lein-pdo "0.1.1"]]}
   :prod { }}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "homepage.core/mount-root"}
     :compiler     {:main                 homepage.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true
                    :preloads             [devtools.preload]
                    :external-config      {:devtools/config {:features-to-install :all}}
                    }}

    {:id           "min"
     :source-paths ["src/cljs"]
     :jar true
     :compiler     {:main            homepage.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :output-dir      "resources/public/js/compiled/out"
                    :asset-path      "js/compiled/out"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}


    ]}

  :main homepage.server

  :aot [homepage.server]

  :uberjar-name "homepage.jar"

  :prep-tasks [["cljsbuild" "once" "min"]["garden" "once"] "compile"]
  )
