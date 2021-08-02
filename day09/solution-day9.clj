

;; Part 1
;;
;; Each number - except one - in the input file is the sum of two of the previous 25 numbers (previous 5 for input-small.txt). Find
;; the one exception. The first 25 (or 5 for input-small.txt) numbers are preamble and are not the number you are looking for.

(defn is-valid-sum [prev-nums num]
    (not (nil? (first  
            (filter ; filter and for are lazy sequences, so this is reasonable performance-wise.
                (fn [n] (and (= (+ (first n) (last n)) num) (not (= (first n) (last n))))) 
                (for [i prev-nums j prev-nums]
                    (list i j)))))))


(defn find-invalid-number [filename preamble-size]
    (with-open [rdr (clojure.java.io/reader filename)]
        (let [
            lines (map read-string (line-seq rdr))
            process-fn (fn [prev-nums next-nums]
                (let [num (first next-nums)]
                    (if (not (is-valid-sum prev-nums num))
                        num
                        (if (nil? (first (rest next-nums)))
                            false
                            (recur 
                                (subvec (conj prev-nums (first next-nums)) 1) 
                                (rest next-nums))))))]
            (process-fn (vec (take preamble-size lines)) (drop preamble-size lines)))))


;; Part 2
;;
;; Find a contiguous sequence of numbers from the input that sum to the answer of part 1 (530627549). Find the sum of the smallest and
;; largest numbers of this set.
;;
;; I am forcing the use of (future) in this solution because I wanted to start experimenting with Clojure's thread system. There is no
;; real reason to use it in this solution - but it does show that you can access one file from two threads. 

(defn find-sequence [filename preamble-size]
    (with-open [rdr (clojure.java.io/reader filename)]
        (let [
            number (future (find-invalid-number filename preamble-size))
            lines (map read-string (line-seq rdr))
            test-sequences (fn [numbers size]
                    (let [seqn (take size numbers)]
                        (cond 
                            (not (= (count seqn) size)) 
                                false ; Have iterated through the list without finding solution
                            (= (apply + seqn) @number)
                                seqn ; Solution
                            :else
                                (recur (rest numbers) size))))]
            (loop [size 2]
                (let [result (test-sequences lines size)]
                (cond 
                    result 
                        result
                    (> size 1000) 
                        false
                    :else
                        (recur (+ size 1))))))))


;; Run Part 1
(prn (find-invalid-number "input.txt" 25))

;; Run Part 2
(let [seqn (find-sequence "input.txt" 25)]
    (prn (+ (apply max seqn) (apply min seqn))))

(shutdown-agents) ; Kills the agent set up by the call to (future) so the program can shut down immediately. 
