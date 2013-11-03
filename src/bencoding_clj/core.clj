(ns bencoding-clj.core
  (:use [clojure.string :only [split]])
  )

(defn enc-by-type)
(defn enc-string [s] (str (.length s) ":" s ))
(defn enc-number [n] (str "i" n "e"))
(defn enc-list [l] (str "l" (apply str (map enc-by-type l)) "e") )
(defn enc-kv [kv] (apply str (map enc-by-type kv)))
(defn enc-map [m] (str "d" (apply str (map enc-kv (sort m))) "e"))
(defn enc-by-type [x]
  (cond
    (string? x) (enc-string x)
    (keyword? x) (enc-string (str x))
    (number? x) (enc-number x)
    (list? x) (enc-list x)
    (vector? x) (enc-list x)
    (map? x) (enc-map x)
    )
  )
(defn encode [x] (enc-by-type x))

(def rs #":")
(def re #"e")

(defn decode-step [] nil)
(defn decode [] nil)

(defn dec-number [s] 
  (let [v   (split s re 2)
        int (Long/parseLong (first v)) ]
        [int (last v)]
    )
  )

(defn dec-string [s] 
  (let [v     (split s rs 2)
        c     (Integer/parseInt (first v))
        rst   (last v)
        word  (apply str (take c rst))
        left  (apply str (drop c rst))
        ]
    [word left]
    )
  )

(defn dec-list [s]
  (let [a (atom '())
        s (atom s)
        v (atom (decode-step @s))
        ]
;  (println "list got: " @a @s @v)
    (while (not (nil? (first @v)))
;    (println "list while: " @a @s @v)
          (swap! a conj (first @v)) 
;      (println "   a:" @a)
          (reset! s (last @v))
;      (println "   s:" @s)
          (reset! v (decode-step @s))
;      (println "   v:"@v)
      )
;    (println "list returns " @a @s)
    [@a (apply str (drop 1 @s))])
  )

(defn dec-map [s]
  (let [a (atom {})
        s (atom s)
        ]
;    (println "map got: " @a @s)
    (while (not (= \e (first @s)))
      (let [
        k (atom (decode-step @s))
        v (atom (decode-step (last @k))) ]
          (swap! a conj [(first @k) (first @v)]) 
;      (println "   a:" @a)
          (reset! s (last @v))
;      (println "   s:" @s)
          (reset! k (decode-step @s)))
      )
    [@a (apply str (drop 1 @s))]
    )
  )

(defn decode-step [s]
  (let [f (first s)
        r (apply str (rest s)) ]
    (cond 
      (= f \i) (dec-number r)
      (= f \l) (dec-list r)
      (= f \d) (dec-map r)
      (= f \e) [nil r]
      :else (dec-string s)
      )
    )
  )

(defn decode [s]
  (let [a (atom '())
        s (atom s)
        ]
  (while (not (empty? @s))
;    (println "step: s=" @s "a=" @a)
    (let [v (decode-step @s)
          ]
;      (println "decoded: " v)
          (swap! a conj (first v))
          (reset! s (last v))
      ))
  @a))

(comment
(encode {"q" "InterfaceController_peerStats" "args" {"page" 0}})
(decode "d3:bar4:spam3:fooi42ee")
(decode "l4:spam6:asdfghe")
(decode "d3:bar4:spam3:fooi42eel4:spam6:asdfghe")
(decode "d4:argsd4:pagei0ee1:q24:Admin_availableFunctionse")
  (def a (atom '())) 
  (def s (atom ""))
  (empty? @s)
  (conj @a "asdf")
(let [v (decode-step "4:spame6:asdfgh")]
  (first v)
  (dec-number "100e")
  )

  (decode "i1383437115371e")
  (Integer/parseInt "1383437115371")
  (Long/parseLong "1383437115371")
  (decode (enc-map {"spam" ["a" "b"]}))
  (encode {"q" "ping", "page" 0})
  (enc-by-type "test")
  (enc-by-type 100)
  (enc-by-type :foo)
  (enc-kv ["key" "val"])
  (enc-string "test")
  (enc-number "100")
  (enc-number "-100")
  (enc-list '("a" "b" "c" "Test"))
  (enc-map {"this" "is" "a" "Test"})
  (enc-map {"spam" ["a" "b"]})
  (apply str (map enc-by-type '("this" :that 100)))
  )
