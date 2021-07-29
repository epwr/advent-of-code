(ns clojure.string)

;; Part 1
;;
;; From the input file, read each group of lines (separated by an empty line). Each line represents someone answering a
;; question. Each letter in the line, represents a question that the person answered 'yes' to. To solve, you need to 
;; calculate how many distinct questions were answered in the affirmative in each group - and then sum these numbers.

(defn part-1-build-group-answers [filename]
    (with-open [rdr (clojure.java.io/reader filename)]
        (def group-answers 
            (reduce 
                (fn [counter line]
                    ; counter is ({current group answers} (list of group answers))
                    (if (= line "")
                        (list ; Empty line. Store group answers, and create new group.
                            {} 
                            (conj (last counter) (first counter)))
                        (list ; Answers. Store each letter in a map and pass on.
                            (reduce 
                                (fn [answers cur-char]    
                                    (assoc answers cur-char cur-char))
                                (first counter)
                                line)
                            (last counter))))
                (list {} `()) 
                (line-seq rdr)))
            (if (first group-answers) ; If there was no empty line at the end of the file.
                (conj (last group-answers) (first group-answers))
                (last group-answers))))

(defn part-1-answer []
    (reduce 
        (fn [sum map] 
            (+ sum (count map))) 
        0 
        (part-1-build-group-answers "input.txt")))


;; Part 2
;;
;; Instead of finding all the question that someone in a group answered yes to, find the number of questions that
;; everyone in a group answered yes to. 
(defn part-2-build-group-answers [filename]
    (with-open [rdr (clojure.java.io/reader filename)]
        (def group-answers 
            (reduce 
                (fn [counter line]
                    ; counter is ({current group answers} (list of group answers))
                    (if (= line "")
                        (list ; Empty line. Store group answers, and create new group.
                            {:group-members 0} 
                            (conj (last counter) (first counter)))
                        (list ; Answers. Store each letter in a map and pass on.
                            (assoc 
                                (reduce 
                                    (fn [answers cur-char] ; Count how many people give the same answer.
                                        (if (answers cur-char)
                                            (assoc answers cur-char (+ (answers cur-char) 1))
                                            (assoc answers cur-char 1)))
                                (first counter)
                                line) :group-members (+ ((first counter) :group-members) 1))
                            (last counter))))
                (list {:group-members 0} `())
                (line-seq rdr)))
            (conj (last group-answers) (first group-answers))))

(defn part-2-answer []
    (reduce 
        (fn [sum map] 
            (+ sum  ; sum + number of questions that everyone answered yes to. 
                (+ -1 ; subtract the :group-members field from the count.
                    (count (filter 
                        (fn [n]  
                            (= (last n) (map :group-members)) ; Check size of group == people who answered yes.
                        ) map))))) 
        0 
        (part-2-build-group-answers "input.txt")))


;; Run Part 1
(println (part-1-answer))

;; Run Part 2
(println (part-2-answer))

