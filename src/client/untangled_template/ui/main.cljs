(ns untangled-template.ui.main
  (:require [om.next :as om :refer-macros [defui]]
            [untangled.client.core :as u]
            [om.dom :as dom]
            [om-css.core :as css :refer-macros [localize-classnames]]
            [untangled.client.mutations :as m]))

(defmethod m/mutate 'test/out [_ _ {:keys [output]}]
  {:action (fn [] (println output))})

(defui ^:once MainPage
  static u/InitialAppState
  (initial-state [this params] {:id :main})
  static om/IQuery
  (query [this] [:id [:current-user '_]])
  static css/CSS
  (css [this] [[(css/local-kw MainPage :x)]])
  static om/Ident
  (ident [this props] [:main :page])
  Object
  (render [this]
    (localize-classnames MainPage
      (let [{:keys [current-user]} (om/props this)]
        (dom/div #js {:class :form} "MAIN PAGE")
        (dom/button #js {:onClick #(om/transact! this `[(untangled/load {:query [:any1]
                                                                         :post-mutation test/out
                                                                         :post-mutation-params {:output 1}})
                                                        (untangled/load {:query [:any2]
                                                                         :post-mutation test/out
                                                                         :post-mutation-params {:output 2}})
                                                        (untangled/load {:query [:any3]
                                                                         :post-mutation test/out
                                                                         :post-mutation-params {:output 3}})])} "TEST")))))

(def ui-main (om/factory MainPage))
