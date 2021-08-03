
;; Part 1
;;
;; You are given a floorplan as input. An \L character represents an empty seat, and a \. represents a floor space.
;; Then, it's basically a game of life, but no one will sit ('live') on the floor spaces. The rules:
;;
;;      1. If a seat is empty (L) and there are no occupied seats adjacent to it, the seat becomes occupied.
;;      2. If a seat is occupied and four or more seats adjacent to it are also occupied, the seat becomes empty.
;;      3. Otherwise, the seat's state does not change.

;; Part 2
;;
;; Instead of caring about the 8 adjacent seats, people now care about the 8 seats they can 'see' (the first non-floor
;; tile in each of the 8 directions from their seat). Additionally, they will now only leave if the see 5 people, rather
;; than four.       

(defn create-floor [filename] 
    (with-open [rdr (clojure.java.io/reader filename)]
        (vec (map (fn [line]
                (vec (map #(if (= % \L) :seat :floor) line)))
            (line-seq rdr)))))

(defn count-adjacent-people [state row col]
    "Counts the :person in the 8 adjacent seats."
    (count 
        (filter
            #(= % :person)
            (for [  r (range (- row 1) (+ row 2))
                    c (range (- col 1) (+ col 2))]
                (if (and (= r row) (= c col))
                    nil ; ignore the center seat.
                    (get (get state r) c))))))

(defn count-adjacent-people-p2 [state row col]
    "Counts the number of :person in the closest seat in each of the 8 directions."
    (count 
        (filter
            #(= % :person)
            (list
                (loop [dist 1] ; ----------------------------------------------- North
                    (let [cell (get (get state (- row dist)) col)]
                        (if (= cell :floor)
                            (recur (+ dist 1)) cell)))
                (loop [dist 1] ; ----------------------------------------------- North-East
                    (let [cell (get (get state (- row dist)) (+ col dist))]
                        (if (= cell :floor)
                            (recur (+ dist 1)) cell)))
                (loop [dist 1] ; ----------------------------------------------- East
                    (let [cell (get (get state row) (+ col dist))]
                        (if (= cell :floor)
                            (recur (+ dist 1)) cell)))
                (loop [dist 1] ; ----------------------------------------------- South-East
                    (let [cell (get (get state (+ row dist)) (+ col dist))]
                        (if (= cell :floor)
                            (recur (+ dist 1)) cell)))
                (loop [dist 1] ; ----------------------------------------------- South
                    (let [cell (get (get state (+ row dist)) col)]
                        (if (= cell :floor)
                            (recur (+ dist 1)) cell)))
                (loop [dist 1] ; ----------------------------------------------- South-West
                    (let [cell (get (get state (+ row dist)) (- col dist))]
                        (if (= cell :floor)
                            (recur (+ dist 1)) cell)))
                (loop [dist 1] ; ----------------------------------------------- West
                    (let [cell (get (get state row) (- col dist))]
                        (if (= cell :floor)
                            (recur (+ dist 1)) cell)))
                (loop [dist 1] ; ----------------------------------------------- North-West
                    (let [cell (get (get state (- row dist)) (- col dist))]
                        (if (= cell :floor)
                            (recur (+ dist 1)) cell)))))))


;; Run Part 1 OR Part 2
;;
;; Use following line to run part 1
;(def rules (list count-adjacent-people 4))
;; Use following line to run part 2
(def rules (list count-adjacent-people-p2 5))


(defn get-next-seat-state [state row col]
    (cond
        (= ((state row) col) :floor)
            :floor
        (= ((first rules) state row col) 0)
            :person
        (>= ((first rules) state row col) (last rules))
            :seat
        :else
            ((state row) col)))

(defn run-tick [state]
    "Generates the next state from the current `state`."
    (vec (for [row (range (count state))]
        (vec (for [col (range (count (first state)))]
            (get-next-seat-state state row col))))))

(defn count-occupied-seats [state]
    (apply +
        0
        (map 
            (fn [row] (count (filter #(= % :person) row)))
            state)))

(defn run-simulation [filename]    
    (loop [
        tick 1 
        last-state (list)
        state (run-tick (create-floor filename))]
            (if (= state last-state)
                (count-occupied-seats state)
                (recur 
                    (+ tick 1)
                    state
                    (run-tick state)))))






;; Run Part 1
(prn (run-simulation "input.txt"))