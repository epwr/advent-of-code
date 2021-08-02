;(require 
;    '[clojure.spec.alpha :as s])



;; Part 1
;;
;; You are given an 'assembly program' which, when executed, runs as an infinite loop. The program contains three operations:
;;
;;      1. 'jmp' jump the provided number of lines (positive = forward, negative = backwards)
;;      2. 'acc' increment an accumulator (which starts as 0) by the given amount. Proceed to the next line.
;;      3. 'nop' proceed to the next line.
;;
;; The first part of the task is to find out what the accumulator is directly before you touch any line for a
;; second time (there are no conditionals, so as soon as you touch an operation for the second time, you are in 
;; an infinite loop). 

(defn parse-input-into-vector [filename]
    (with-open [rdr (clojure.java.io/reader filename)]
        (reduce
            (fn [aggr line]
                (conj aggr 
                    (mapv (fn [n] n) (rest (first (re-seq #"^(\w+) ([+-]\d+)" line))))))
            []
            (line-seq rdr))))

(defn run-program [lines cur-line acc]
    (if (or (nil? (get lines cur-line)) (get (lines cur-line) 2))
        (if (nil? (get lines cur-line)) ; program finished or in infinite loop, so return
            (list "Finished" acc)
            (list "Error" acc)) 
        (case (get (lines cur-line) 0)
            "jmp" (run-program (assoc lines cur-line (assoc (lines cur-line) 2 true)) (+ cur-line (read-string (get (lines cur-line) 1))) acc)
            "acc" (run-program (assoc lines cur-line (assoc (lines cur-line) 2 true)) (+ cur-line 1) (+ acc (read-string (get (lines cur-line) 1))))
            "nop" (run-program (assoc lines cur-line (assoc (lines cur-line) 2 true)) (+ cur-line 1) acc))))

;; Part 2
;;
;; There is exactly one operation that has been corrupted, and this is causing the infinite loop. It is either a 'nop' that has 
;; become a 'jmp' or a 'jmp' that has become a 'nop'. Part 2 of the task is to figure out which operation has been corrupted, and
;; then run the program and find the final value of the accumulator.

(defn fix-program-using-loop [lines cur-line]
    (cond
        (= (get lines cur-line) nil) 
            (run-program lines 0 0) ; no fix was found, run original and return.
        (= (first (lines cur-line)) "jmp") 
            (let ; flip jmp to nop and run. return on success.
                [result (run-program (assoc lines cur-line (assoc (lines cur-line) 0 "nop")) 0 0)]
                (if (= (first result) "Finished")
                    (last result)
                    (recur lines (+ cur-line 1))))
        (= (first (lines cur-line)) "nop") 
            (let ; flip nop to jmp and run. return on success.
                [result (run-program (assoc lines cur-line (assoc (lines cur-line) 0 "jmp")) 0 0)]
                (if (= (first result) "Finished")
                    (last result)
                    (recur lines (+ cur-line 1))))
        :else
            (recur lines (+ cur-line 1))
    ))

(defn fix-program-using-loop-2 [lines cur-line]
    (cond
        (= (get lines cur-line) nil) 
            (run-program lines 0 0) ; no fix was found, run original and return.
        (= (first (lines cur-line)) "jmp") 
            (let ; flip jmp to nop and run. return on success.
                [result (run-program (assoc lines cur-line (assoc (lines cur-line) 0 "nop")) 0 0)]
                (if (= (first result) "Finished")
                    (last result)
                    (fix-program-using-loop-2 lines (+ cur-line 1))))
        (= (first (lines cur-line)) "nop") 
            (let ; flip nop to jmp and run. return on success.
                [result (run-program (assoc lines cur-line (assoc (lines cur-line) 0 "jmp")) 0 0)]
                (if (= (first result) "Finished")
                    (last result)
                    (fix-program-using-loop-2 lines (+ cur-line 1))))
        :else
            (fix-program-using-loop-2 lines (+ cur-line 1))
    ))



;; Run Part 1
(prn (last (run-program (parse-input-into-vector "input.txt") 0 0)))    

;; Run Part 2
(println)
(println "Running Part 2 using 'recur': ")
(prn (time (fix-program-using-loop (parse-input-into-vector "input.txt") 0)))

(println)
(println "Running Part 2 using normal recursion: ")
(prn (time (fix-program-using-loop-2 (parse-input-into-vector "input.txt") 0)))
