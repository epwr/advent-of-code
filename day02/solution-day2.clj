(ns clojure.string)


;; Part 1
;;
;; From a file that contains lines that represent a 'policy' and a string, count how many strings satisfy the policy. For example, one line is:
;;      9-12 q: qqqxhnhdmqqqqjz
;; This means that for the string "qqqxhnhdmqqqqjz" to be valid, it must have between 9 and 12 'q's. This string is not valid.
(defn count-valid-passwords [filename f]
    (with-open [rdr (clojure.java.io/reader filename)]
        (count (filter f (line-seq rdr)))))

(defn policy-v1-filter [line]
    (def policy (subvec (first (re-seq #"(\d+)-(\d+) (\w): (.+)$" line)) 1)) 
    (def times-found (count (re-seq (re-pattern (get policy 2)) (get policy 3))))
    (and 
        (>= times-found (read-string (get policy 0)))
        (<= times-found (read-string (get policy 1)))))

;; Part 2
;;
;; Count the strings that satisfy the new policy form. Given:
;;      9-12 q: qqqxhnhdmqqqqjz
;; A q must be in either position 9 or position 12 of the string (exclusive or). Positions are indexed from
;; 1 (not from 0).
(defn xor [x y]
    (and 
        (or x y)
        (not (and x y))))


(defn policy-v2-filter [line]
    (def policy (subvec (first (re-seq #"(\d+)-(\d+) (\w): (.+)$" line)) 1)) 
    (xor 
        (= (str (get (get policy 3) (- (read-string (get policy 0)) 1))) (get policy 2)  )
        (= (str (get (get policy 3) (- (read-string (get policy 1)) 1))) (get policy 2)  )))

;; Run Part 1
(println (count-valid-passwords "input-day2.txt" policy-v1-filter))

;; Run Part 2
(println (count-valid-passwords "input-day2.txt" policy-v2-filter))