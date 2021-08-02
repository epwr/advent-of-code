

;; Part 1
;;
;; The input is a list of numbers that represents the output 'joltage' of all the power adapters that you have. Each adapter can raise the joltage by up to 3 jolts
;; (meaning it can accept input joltage of 1, 2, or 3 jolts below its output). Find a way to use all of your adapters to start from 0 jolts and power your laptop 
;; which requires 3 jolts more than your highest adapter (it has its own adapter). Count the number of times you raise the joltage by 1 jolt, and the number of times
;; you raise the joltage by 3 jolts.
;;
;; You need to consider the jump from 0 to the first adapter, and the last adapter to your computer (a 3 jolt jump).

(defn get-sorted-joltages [filename]
    "Returns a sorted list of numbers from provided file. Assumes each line in the file is a valid number."
    (with-open [rdr (clojure.java.io/reader filename)]
        (sort (map read-string (line-seq rdr)))))

(defn count-differences [numbers diffs]
    "Takes 'number` - a sorted list of integers - and `diffs` - a map where the keys are the difference between two adjacent numbers, and the values are the count of that difference."
    (if (empty? (rest numbers)) 
        diffs
        (let [diff (- (first (rest numbers)) (first numbers))]
            (if (diffs diff)
                (recur 
                    (rest numbers) 
                    (assoc diffs diff (+ 1 (diffs diff))))
                (recur 
                    (rest numbers) 
                    (assoc diffs diff 1))))))


;; Part 2
;;
;; Count the number of distinct ways that you can power your laptop. There are more than a trillion ways, so the algorithm must be
;; reasonably efficient.
;;
;; My algorithm relies on the idea that if node F can only be reached from node D and E, then the number of paths to node F is the sum
;; of the paths to D and the paths E.

(defn count-paths [filename]
    "Finds the paths in O(n) time by determining how many paths there to each node, only keeping the number of paths to nodes that are still relevant."
    (let [numbers (get-sorted-joltages filename)]
        (last (first (reduce
            (fn [cur-paths cur-num]
                (let [next-paths (filter (fn [n] (<= (- cur-num (first n)) 3)) cur-paths)]
                    (conj
                        next-paths
                        (list cur-num 
                            (reduce
                                (fn [sum path] (+ sum (last path)))
                                0
                                next-paths)))))
            (list (list 0 1))
            numbers)))))

;; Run Part 1
(prn (count-differences (get-sorted-joltages "input.txt") {1 1, 3 1}))

;; Run Part 2
(prn (count-paths "input.txt"))

