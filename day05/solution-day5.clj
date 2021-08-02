(ns clojure.string)

;; Part 1
;;
;; Convert a list of strings into binary numbers. The first 8 letters have F = 0, B = 1, and then the last 
;; two letters have L = 0, R = 1. Find the highest value.
(defn convert-string-to-number [string]
    (reduce 
        (fn [number next-char]
        (if (or (= next-char \B) (= next-char \R)) 
            (+ (* number 2) 1)
            (* number 2)))
        0 string))


(defn part-1-build-number-list [filename]
    (with-open [rdr (clojure.java.io/reader filename)]
        (reduce 
            (fn [list value]
                (cons (convert-string-to-number value) list))
            (list) 
            (line-seq rdr))))

;; Part 2
;;
;; Find the missing number from the list (without relying on a specific start or end number).
(defn get-missing-number [unsorted-list]
    (first (last (reduce 
        (fn [counter cur-value]
            ; counter is (last-value [list of missing values])
            (if (= (+ (first counter) 2) cur-value)
                (list cur-value (cons (+ (first counter) 1) (last counter)))
                (list cur-value (last counter))))
        (list -2 (list))
        (sort unsorted-list)))))


;; Run Part 1
(println (apply max (part-1-build-number-list "input.txt")))

;; Run Part 2
(println(get-missing-number (part-1-build-number-list "input.txt")))