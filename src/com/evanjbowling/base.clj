(ns com.evanjbowling.base
  (:require
   [clojure.math.numeric-tower :as math]))

(defn ^:private parts
  "Split a decimal into integer and fraction parts"
  [^BigDecimal d]
  (let [i (.divideToIntegralValue d 1M)]
    [(bigint i) (-' d i)]))

(defn ^:private max-digit
  [i base idx]
  (loop [d 1]
    (if (and (<= (*' d (math/expt base idx)) i)
             (< i (*' (inc d) (math/expt base idx))))
      d
      (recur (inc d)))))

(defn ^:private int-to-base*
  [i base]
  (if (<= i (dec base))
    {0 i}
    (loop [k 1
           prev-max (dec base)]
      (let [max-val-w-curr-k (+' prev-max
                                 (*' (dec base)
                                     (math/expt base k)))]
        (if (<= i max-val-w-curr-k)
          {k (max-digit i base k)}
          (recur (inc k) max-val-w-curr-k))))))

(defn ^:private map-digit
  [d]
  (if (< d 10)
    d
    (get {10 \a, 11 \b, 12 \c
          13 \d, 14 \e, 15 \f}
         d)))

(defn ^:private reduce-digits
  [digits]
  (loop [digit-indices (if (empty? digits)
                         (range 1)
                         (->> digits
                              keys
                              (apply max)
                              inc
                              range))
         result ""]
    (if (empty? digit-indices)
      result
      (let [unmapped-digit
            (if-let [d (get digits (first digit-indices))]
              d
              0)]
        (recur (rest digit-indices)
               (str (map-digit unmapped-digit) result))))))

(defn ^:private int-to-base
  "Convert integer portion to other base."
  [i base]
  {:pre (< 1 base)}
  (loop [left i
         digits {}]
    (if (= 0 left)
      (reduce-digits digits)
      (let [next-digit (int-to-base* left base)
            [e d] ((juxt (comp first keys) (comp first vals)) next-digit)]
        (recur (-' left (*' d (math/expt base e)))
               (merge digits next-digit))))))

(defn ^:private fraction-to-base
  "Convert fractional portion d >= 0 and d < 1
   to other base. First form extracts 10 digits.
  Second form extracts n digits."
  ([d base]
   (fraction-to-base d base 10))
  ([d base num-digits]
   (loop [n 0
          result (if (= 0M d) "0" "")
          left d]
     (if (or (= 0M left) (= n num-digits))
       {:value result, :remainder left}
       (let [[i f] (parts (*' base left))]
         (recur (inc n) (str result (map-digit i)) f))))))

(defn to-base
  "Convert decimal value into another base
  representation."
  [d base]
  (let [d (bigdec d)
        base (long base)
        [i f] (parts d)]
    (format "%s.%s"
            (int-to-base i base)
            (:value (fraction-to-base f base)))))

