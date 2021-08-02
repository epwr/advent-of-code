(ns clojure.string)


;; Part 1
;;
;; From a file that contains dots (.) to represent empty space and hashs (#) to represent trees, find how many trees
;; you would collide with when starting in the top-left and going down one row, and right 3 elements (wrapping around
;; on the same line).
(defn part-1-count-collisions [filename f]
    (with-open [rdr (clojure.java.io/reader filename)]
        (first (reduce f (list 0 0) (line-seq rdr)))))

(defn part-1-reducer [counter line]
    ; counter is (collisions current-column)
    (if (= (get line (mod (last counter) (count line))) \#)
        (list (+ (first counter) 1) (+ (last counter) 3))
        (list    (first counter)    (+ (last counter) 3))))

;; Part 2
;;
;; Same as part 1, except for 5 different slopes. Print out the product of all 5 answers. Slopes:
;;      - Right 1, down 1
;;      - Right 3, down 1
;;      - Right 5, down 1
;;      - Right 7, down 1
;;      - Right 1, down 2
(defn part-2-count-collisions [filename f]
    (with-open [rdr (clojure.java.io/reader filename)]
        (first (reduce f (list (list 0 0 0 0 0) 0) (line-seq rdr)))))

(defn part-2-reducer [counter line]
    ; counter is ((#-of-collisions) current-row)
    (list (list
        ;; Right 1, down 1
        (if (= (get line (mod (last counter) (count line))) \#)
                (+ (nth (first counter) 0) 1)
                (nth (first counter) 0))
        ;; Right 3, down 1
        (if (= (get line (mod (* (last counter) 3) (count line))) \#)
                (+ (nth (first counter) 1) 1)
                (nth (first counter) 1))
         ;; Right 5, down 1
        (if (= (get line (mod (* (last counter) 5) (count line))) \#)
                (+ (nth (first counter) 2) 1)
                (nth (first counter) 2))
        ;; Right 7, down 1
        (if (= (get line (mod (* (last counter) 7) (count line))) \#)
                (+ (nth (first counter) 3) 1)
                (nth (first counter) 3))
        ;; Right 1, down 2
        (if (= (mod (last counter) 2) 0) ; If row # is even
            (if (= (get line (mod (/ (last counter) 2) (count line))) \#)
                (+ (nth (first counter) 4) 1)
                (nth (first counter) 4))
            (nth (first counter) 4))
    ) (+ (last counter) 1)))

;; Run Part 1
(println (part-1-count-collisions "input.txt" part-1-reducer))

;; Run Part 2
(println (reduce * 1 (part-2-count-collisions "input.txt" part-2-reducer)))