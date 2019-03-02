(ns com.evanjbowling.base.convert
  (:require
   [clojure.math.numeric-tower :as math])
  (:import
   (java.math BigDecimal)))

(defn split'
  "Split decimal into integral and fractional parts.
  Support arbitrary precision."
  [^BigDecimal d]
  (let [i (.divideToIntegralValue d 1M)]
    {:com.evanjbowling.base/integer (bigint i)
     :com.evanjbowling.base/fraction (-' d i)}))

(defn split
  "Split decimal into integral and fractional parts.
  Will throw if integer portion is larger than a long
  can hold."
  [^BigDecimal d]
  (let [i (.divideToIntegralValue d 1M)]
    {:com.evanjbowling.base/integer (.longValueExact i)
     :com.evanjbowling.base/fraction (-' d i)}))

(defn ^:private max-digit
  [i base e]
  (loop [d 1]
    (if (and (<= (*' d (math/expt base e)) i)
             (< i (*' (inc d) (math/expt base e))))
      d
      (recur (inc d)))))

(defn ^:private int-to-base*
  [i base]
  (if (<= i (dec base))
    {0 i}
    (loop [e 1
           prev-max (dec base)]
      (let [max-val-w-curr-e (+' prev-max
                                 (*' (dec base)
                                     (math/expt base e)))]
        (if (<= i max-val-w-curr-e)
          {e (max-digit i base e)}
          (recur (inc e) max-val-w-curr-e))))))

(defn ^:private reduce-digits
  [digits]
  (loop [digit-indices (if (empty? digits)
                         (range 1)
                         (->> digits
                              keys
                              (apply max)
                              inc
                              range))
         result []]
    (if (empty? digit-indices)
      result
      (let [digit
            (if-let [d (get digits (first digit-indices))]
              d
              0)]
        (recur (rest digit-indices) (cons digit result))))))

(defn int-to-base
  "Convert integer portion to other base."
  [i base]
  (loop [left i
         digits {}]
    (if (= 0 left)
      (reduce-digits digits)
      (let [next-digit (int-to-base* left base)
            [e d] ((juxt (comp first keys) (comp first vals)) next-digit)]
        (recur (-' left (*' d (math/expt base e)))
               (merge digits next-digit))))))

(defn fraction-to-base
  "Returns a lazy sequence of the fractional values where
  each v >= 0 and < base. Note that this may be an infinite
  sequence."
  ([^BigDecimal d base]
   (lazy-seq
    (if (= 0M d)
      [0]
      (let [{i :com.evanjbowling.base/integer
             f :com.evanjbowling.base/fraction}
            (split (*' base d))]
        (cons i (if (= 0M f)
                  []
                  (fraction-to-base f base))))))))
