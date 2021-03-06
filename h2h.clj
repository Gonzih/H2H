(ns h2h
  (:gen-class))

(comment
  html2hiccup.core=> (def html "<a>asd</a><img<v>valid tags only</v>>dsa</img>")
  #'html2hiccup.core/html
  html2hiccup.core=> html
  "<a>asd</a><img<v>valid tags only</v>>dsa</img>"
  html2hiccup.core=> (h2h html)
  ([:a "asd"] [:v "valid tags only"])
  html2hiccup.core=>
  ) ;; COMMENT

(defn html2hiccup
  "Converts full tag elemnts <t></t>"
  [str-html]
  (let
      [tags-open (map #(hash-map
                        (keyword (last %))
                        '{})
                      (re-seq #"<(\w+)>"
                              str-html))
       tags-close (map #(hash-map
                         (keyword (last %))
                         '{})
                       (re-seq #"<(/\w+)>"
                               str-html))
       tags-content (let
                        [tags (lazy-seq
                               (.split str-html
                                       ">"))
                         stags (filter #(.contains
                                         (str %)
                                         "</")
                                       tags)]
                      (map #(first
                             (.split
                              (str %)
                              "<"))
                           stags))
       html-tags (partition 3
                            (interleave
                             (flatten tags-content)
                             (flatten tags-open)
                             (flatten tags-close)))]
     (lazy-seq
      (for [ht html-tags
            :let
            [content (first ht)
             opent (first
                    (keys
                     (second ht)))
             closet (last ht)]]
        [opent
         content]))))
        
(defn h2h [str-html] (html2hiccup str-html))