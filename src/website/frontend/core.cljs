(ns website.frontend.core
  (:require [taoensso.timbre :as log]
            [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [GET POST] :as ajax]
            ))

(enable-console-print!)

(println "This text is printed from src/website/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

;; TODO change back to defonce when required
(def app-state (atom
                 {}
                 )
  )
(defonce systemInfo
  (atom {:context (-> js/window
                      .-location
                      ;; This gets /context/index.html
                      .-pathname
                      ;; Remove the ending
                      (clojure.string/replace "/index.html" "")
                      )
         :buttonSize " btn-xs "
         }
        )
  )
(def linkItems
  "Links and their content"
  {:hphelper {:title "Paranoia hphelper"
              :content [:span "The High Programmer Helper allows game masters to prepare for games in minutes, for both online and offline games."]
              :link "/hphelper/"
              }
   :acmoi {:title "Paranoia moi"
           :content [:span "A work in progress game of tracking down treasonous behaviour and covering up your own"]
           :link "/acmoi/index.html"
           }
   :yasg {:title "YASG"
          :content [:span "Yet Another Sodoku Generator. Generates both normal and rectangular sodokus."]
          :link "https://github.com/lsenjov/YASG"
          }
   :frd {:title "Fortune Runs Deep"
         :content [:span "A spaghetti western role playing game of high tension and pushing your luck. A work in progress"]
         :link "https://github.com/lsenjov/fortune-runs-deep"
         }
   :gamebot {:title "Slack Gamebot"
             :content [:span "A gamebot for slack. Currently has chess, werewolf, and spyfall. Created to allow for the easy creation of new games, and so far works wonders."]
             :link "https://github.com/lsenjov/clj-slackbot"
             }
   :github {:title "Github"
            :content [:span "Most of these projects are on github for your viewing pleasure."]
            :link "https://github.com/lsenjov"
            }
   :twitter {:title "Twitter"
             :content [:span "Mostly unused, but easiest way to get in contact"]
             :link "https://twitter.com/LSenjov"
             }
   }
  )

(defn- wrap-context
  "Adds the current context to a url"
  [url]
  (str (:context @systemInfo) url)
  )

(defn- links-button-component
  "A button that changes according to the state of the atom"
  [^Atom at ^Keyword kw ^String title]
  [:span {:class (if (= kw @at) "btn btn-default btn-success" "btn btn-default")
          :onClick #(reset! at kw)
          }
   title
   ]
  )

(defn- links-component
  "Links to all the other projects in tabs"
  []
  (let [tab (atom :hphelper)]
    (fn []
      [:div
       [:div {:class "btn-group btn-group-justified"}
        (doall (for [[k {:keys [title content]}] linkItems]
                 ^{:key k}
                 [links-button-component tab k title]
                 )
               )
        ]
       [:div {:class "jumbotron"}
        (get-in linkItems [@tab :content])
        [:br]
        [:a {:class "btn btn-default btn-success"
             :href (get-in linkItems [@tab :link])
             :target (name @tab)
             }
         "Link"
         ]
        ]
       ]
      )
    )
  )
(defn front
  "The first container"
  []
  (fn []
    [:div
     [:h2 "I create stuff"]
     [links-component]
     ]
    )
  )


(reagent/render-component [front]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
