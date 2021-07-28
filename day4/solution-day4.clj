(ns clojure.string)



;; Part 1
;;
;; From an input file, 'passports' are collections of key:value pairs. The k:v pairs are separated by
;; spaces or newlines, while the passports are separated by empty lines.
;;
;; Valid passports have every field listed, except the CID field which is optional. Count the number of valid
;; passports.
(defn part-1-is-valid-passport [passport]
    (every? #(contains? passport %) (list "byr" "iyr" "eyr" "hgt" "hcl" "ecl" "pid")))


(defn part-1-count-valid-passports [filename f]
    (with-open [rdr (clojure.java.io/reader filename)]
        (def retval (reduce f (list {} 0 0) (line-seq rdr)))
        (if (part-1-is-valid-passport (first retval))
            (+ (nth retval 1) 1)
            (nth retval 1))))

(defn part-1-reducer [counter line]
    ; counter is ({current passport fields} valid-passport-count current-line)
    (if (= line "")
        (list ; Empty line, test if valid passport and reset current passport.
            {} 
            (if (part-1-is-valid-passport (first counter))
                (+ (nth counter 1) 1)
                (nth counter 1)) 
            (+ (last counter) 1)) 
        (list 
            (reduce    
                (fn [map values]
                    (assoc map (get values 1) (get values 2))) 
                (first counter) (re-seq #"(\w+):([#\w]+)" line))
            (nth counter 1)
            (+ (last counter) 1))))

;; Part 2
;;
;; Same as part 1, except perform some data validation.
(defn part-2-is-valid-passport [passport]
    (cond
        (not (and  ; 1920 <= byr <= 2002
            (contains? passport "byr")
            (>= (read-string (passport "byr")) 1920)
            (<= (read-string (passport "byr")) 2002)))
        false
        (not (and
            (contains? passport "iyr")
            (>= (read-string (passport "iyr")) 2010)
            (<= (read-string (passport "iyr")) 2020)))
        false
        (not (and
            (contains? passport "eyr")
            (>= (read-string (passport "eyr")) 2020)
            (<= (read-string (passport "eyr")) 2030)))
        false
        (not (and
            (contains? passport "hgt")
            (let [matches (first (re-seq #"^(\d+)(cm|in)" (passport "hgt")))]
                (if matches 
                    (if (= (last matches) "in")
                        (and
                            (>= (read-string (nth matches 1)) 59)
                            (<= (read-string (nth matches 1)) 76))
                        (and
                            (>= (read-string (nth matches 1)) 150)
                            (<= (read-string (nth matches 1)) 193)))
                    false))))
        false
        (not (and 
            (contains? passport "hcl")
            (re-seq #"#[a-f|\d]{6}" (passport "hcl"))))
        false
        (not (and
            (contains? passport "ecl")
            (includes? (list "amb" "blu" "brn" "gry" "grn" "hzl" "oth") (passport "ecl"))
            ))
        false
        (not (and
            (contains? passport "pid")
            (re-seq #"^\d{9}$" (passport "pid"))))
        false
        :else
        true))

(defn part-2-count-valid-passports [filename f]
    (with-open [rdr (clojure.java.io/reader filename)]
        (def retval (reduce f (list {} 0 0) (line-seq rdr)))
        (if (part-2-is-valid-passport (first retval))
            (+ (nth retval 1) 1)
            (nth retval 1))))

(defn part-2-reducer [counter line]
    ; counter is ({current passport fields} valid-passport-count current-line)
    (if (= line "")
        (list ; Empty line, test if valid passport and reset current passport.
            {} 
            (if (part-2-is-valid-passport (first counter))
                (+ (nth counter 1) 1)
                (nth counter 1)) 
            (+ (last counter) 1)) 
        (list 
            (reduce    
                (fn [map values]
                    (assoc map (get values 1) (get values 2))) 
                (first counter) (re-seq #"(\w+):([#\w]+)" line))
            (nth counter 1)
            (+ (last counter) 1))))


;; Run Part 1
(println (part-1-count-valid-passports "input.txt" part-1-reducer))

;; Run Part 2
(println (part-2-count-valid-passports "input.txt" part-2-reducer))