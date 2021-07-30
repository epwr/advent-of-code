(ns clojure.string)
(require 
    '[clojure.set :as set]
    '[clojure.spec.alpha :as s])


;; Specs
(s/def :part-1/rule (s/coll-of string?))



;; Part 1
;;
;; Each input line contains a rule of which bags can be contained by which bags. The goal is to check how many different colours of bags
;; could hold a shiny gold bag.
;;
;; My code creates an adjacency list for a direct graph (edges go from one colour to a colour that can hold it). It operates under the
;; that there are no cycles in the graph.


;; Extend a map of bag-colour:valid-containers with the rules contained in a line of input.
(defn parse-contained-by-rule [rules line]
    (def bags-list (re-seq 
            #"(\d+) ([a-z]+ [a-z]+ bag)|(^[a-z]+ [a-z]+ bag)|(no other bags)"
            line))
    (def container (first (first bags-list)))
    (reduce
        (fn [rules cur-rule]
            (if (nth cur-rule 2)
                (if (rules (nth cur-rule 2))
                    (assoc rules (nth cur-rule 2) (conj (rules (nth cur-rule 2)) container))
                    (assoc rules (nth cur-rule 2) [container]))
                rules)) ; is not a valid bag (eg. 'no other bags')
        rules
        (rest bags-list)))

;; Build a directed adjacency list which maps bag colour -> [valid containers].
(defn build-contained-by-rules [filename]
    (with-open [rdr (clojure.java.io/reader filename)]
        (reduce 
            parse-contained-by-rule
            {}
            (line-seq rdr))))

(defn calculate-bags-that-hold-colour [bag-colour rules]
    (reduce
        (fn [containers current-bag-colour]
            (if (rules current-bag-colour)
                (set/union #{current-bag-colour} (calculate-bags-that-hold-colour current-bag-colour rules) containers)
                (set/union #{current-bag-colour} containers)))
        #{}
        (rules bag-colour)))
 
;; Part 2
;;
;; Using the same rules, check how many bags a shiny gold bag must use.

;; Parse line by line to create a mapping container -> {bag-colour required-number ...}
(defn parse-contains-rule [rules line]
    (def bags-list (re-seq 
            #"(\d+) ([a-z]+ [a-z]+ bag)|(^[a-z]+ [a-z]+ bag)|(no other bags)"
            line))
    (def container (first (first bags-list)))
    (reduce
        (fn [rules cur-rule]
            (if (nth cur-rule 2) ; if has valid contents.
                (if (rules container) ; if container already has rules.
                    (assoc rules container (assoc (rules container) (nth cur-rule 2) (nth cur-rule 1)))
                    (assoc rules container {(nth cur-rule 2) (nth cur-rule 1)}))
                rules))
        rules
        (rest bags-list)))


;; Build a directed adjacency list which maps bag-colour -> contents.
(defn build-contains-rules [filename]
    (with-open [rdr (clojure.java.io/reader filename)]
        (reduce 
            parse-contains-rule
            {}
            (line-seq rdr))))

;; Calculates the number of bags you have given a specific bag (the count includes the starting bag)
(defn calculate-number-of-bags [bag-colour rules]
    (reduce
        (fn [count kv]
            (+ count (* 
                    (read-string (last kv))
                    (calculate-number-of-bags (first kv) rules))))
        1
        (rules bag-colour)))


;; Run Part 1
(println (count (calculate-bags-that-hold-colour "shiny gold bag" (build-contained-by-rules "input.txt"))))       


;; Run Part 2
; - 1 to result because result includes the shiny gold bag
(prn (- (calculate-number-of-bags "shiny gold bag" (build-contains-rules "input.txt")) 1))