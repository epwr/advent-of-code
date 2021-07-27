
(ns clojure.string)

;; Solution to Part 1
;;
;; Find two numbers in the input.txt which sum to 2020.
(defn find-two-elements-which-sum [sum hmap seq]
    (cond 
        (nil? (first seq)) 
            nil
        (contains? hmap (first seq)) 
            (list (first seq) (- sum (first seq)))
        :else
            (find-two-elements-which-sum sum (assoc hmap (- sum (first seq)) 0) (rest seq))
    )
)



;; Solution to Part 2
;;
;; Find three numbers in input.txt that sum to 2020.
;; Did not know this problem until part 1 was solved.
(defn find-three-elements-which-sum [sum seq]
    (def result (find-two-elements-which-sum (- sum (first seq)) {} (rest seq)))
    (if result
        (cons (first seq) result)   
        (find-three-elements-which-sum sum (rest seq))
    )
)


;; Run Part 1
(def result1 (find-two-elements-which-sum 2020 {} (map read-string (split (slurp "input-day1.txt") #"\n"))))
(println
    result1
    "which multiply to: "
    (reduce * result1)
)

;; Run Part 2
(def result2 (find-three-elements-which-sum 2020 (map read-string (split (slurp "input-day1.txt") #"\n"))))
(println
    result2
    "which multiply to: "
    (reduce * result2)
)