
;; Part 1
;;
;; You are a ship and are given a set of instructions. N10 means move North 10 of some magical unit, S5 means move
;; South by 5 of those magical units, L90 means turn left 90 degrees (you start facing east, moving north/south/east/west
;; does not involve turning), and moving F8 means move 8 magical units forwards (in whatever direction you are facing).
;; Find the manhatten distanct (sum of the absolute distances from the origin along the x and y axes) of your final position.
;;
;; Note: From checking the input.txt file, it appears you only turn right and left by 90 * n degrees.

(defn get-initial-state-p1 []
    "Create an initial `state` (a map) for part 1."
    {:angle 0, :x-ship 0, :y-ship 0})

(defn get-cur-direction [state]
    "Convert the :angle in `state` into a cardinal direction."
    ({0 "E", 90 "N", 180 "W", 270 "S"}
        (mod (state :angle) 360)))

(defn get-manhattan-distance [state]
    "Calculate the manhattan distance of the ship from the provided `state`."
    (+ (Math/abs (state :y-ship)) (Math/abs (state :x-ship))))

(defn tick-p1 [state line]
    "Create a new `state` based on command provided, and the rules in part 1."
    (let [re-cmd (first (re-seq #"(.)(\d+)" line))
          cmd (if (= (get re-cmd 1) "F") 
                (list (get-cur-direction state) (read-string (get re-cmd 2)))
                (list (get re-cmd 1) (read-string (get re-cmd 2))))]
    (cond
        (not (= (count cmd) 2))
            (throw (AssertionError. (str "Invalid `line` passed to user/tick. -- " line)))
        (= (first cmd) "L")
            (assoc state :angle (+ (state :angle) (nth cmd 1)))
        (= (first cmd) "R")
            (assoc state :angle (- (state :angle) (nth cmd 1)))
        (= (first cmd) "N")
            (assoc state :y-ship (+ (state :y-ship) (nth cmd 1)))
        (= (first cmd) "E")
            (assoc state :x-ship (+ (state :x-ship) (nth cmd 1)))
        (= (first cmd) "S")
            (assoc state :y-ship (- (state :y-ship) (nth cmd 1)))
        (= (first cmd) "W")
            (assoc state :x-ship (- (state :x-ship) (nth cmd 1)))
        :else
            (throw (AssertionError. (str "Invalid `line` passed to user/tick (Direction not recognized). -- " cmd))))))

(defn run-simulation [filename state tick-fn]
    "Simulates having the ship following the commands provided in `filename`, using the provided `tick-fn` function. Returns the final `state`."
    (with-open [rdr (clojure.java.io/reader filename)]
        (get-manhattan-distance (reduce
            tick-fn
            state
            (line-seq rdr)))))

;; Part 2
;;
;; You misinterpreted the commands! A waypoint starts 10 units east and 1 unit north of the ship. N10 means move the
;; waypoint north by 10 units (similar for S10/E10/W10). L90 means rotate the waypoint around the ship by 90 degrees.
;; F5 means move to the waypoint 5 times, so if the waypoint is 3 north, 1 east of the ship, then F5 would cause the
;; ship to move by 15 north, 5 east. Find the manhatten distanct of your final position.

(defn get-initial-state-p2 []
    "Create an initial `state` (a map) for part 2."
    {:x-ship 0, :y-ship 0, :x-wp 10, :y-wp 1})

(defn rotate-waypoint [state cmd]
    "Rotates the waypoint (from `state`) per the provided `cmd` (a sequence of L or R and the angle). Returns a new state."
    (loop [ new-state state
            angle (nth cmd 1)
            angle-dir (if (= (nth cmd 0) "L") -1 1)]
        (if (> angle 0)
            (recur
                (assoc new-state 
                    :x-wp (* angle-dir (new-state :y-wp))
                    :y-wp (* (* angle-dir -1) (new-state :x-wp)))
                (- angle 90)
                angle-dir)
            new-state)))


(defn tick-p2 [state line]
    "Create a new `state` based on command provided, and the rules in part 2."
    (let [re-cmd (first (re-seq #"(.)(\d+)" line))
          cmd (list (get re-cmd 1) (read-string (get re-cmd 2)))]
    (cond
        (not (= (count cmd) 2))
            (throw (AssertionError. (str "Invalid `line` passed to user/tick. -- " line)))
        (= (first cmd) "F")
            (assoc state
                :x-ship (+ (* (state :x-wp) (nth cmd 1)) (state :x-ship))
                :y-ship (+ (* (state :y-wp) (nth cmd 1)) (state :y-ship)))
        (= (first cmd) "L")
            (rotate-waypoint state cmd)
        (= (first cmd) "R")
            (rotate-waypoint state cmd)
        (= (first cmd) "N")
            (assoc state :y-wp (+ (state :y-wp) (nth cmd 1)))
        (= (first cmd) "E")
            (assoc state :x-wp (+ (state :x-wp) (nth cmd 1)))
        (= (first cmd) "S")
            (assoc state :y-wp (- (state :y-wp) (nth cmd 1)))
        (= (first cmd) "W")
            (assoc state :x-wp (- (state :x-wp) (nth cmd 1)))
        :else
            (throw (AssertionError. (str "Invalid `line` passed to user/tick (Direction not recognized). -- " cmd))))))


;; Run Part 1
(prn (run-simulation "input.txt" (get-initial-state-p1) tick-p1))

;; Run Part 2
(prn (run-simulation "input.txt" (get-initial-state-p2) tick-p2))
