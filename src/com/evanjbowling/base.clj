(ns com.evanjbowling.base
  (:require
   [clojure.math.numeric-tower   :as math]
   [clojure.string               :as string]
   [com.evanjbowling.base.format :as fmt])
  (:import
   (java.math BigDecimal)))

(defn ^:private split'
  "Split decimal into integral and fractional parts.
  Support arbitrary precision."
  [^BigDecimal d]
  (let [i (.divideToIntegralValue d 1M)]
    {::integer (bigint i)
     ::fraction (-' d i)}))

(defn ^:private split
  "Split decimal into integral and fractional parts.
  Will throw if integer portion is larger than a long
  can hold."
  [^BigDecimal d]
  (let [i (.divideToIntegralValue d 1M)]
    {::integer (.longValueExact i)
     ::fraction (-' d i)}))

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

(defn ^:private int-to-base
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

(defn ^:private fraction-to-base
  "Returns a lazy sequence of the fractional values where
  each v >= 0 and < base. Note that this may be an infinite
  sequence."
  ([^BigDecimal d base]
   (lazy-seq
    (if (= 0M d)
      [0]
      (let [{i ::integer, f ::fraction} (split (*' base d))]
        (cons i (if (= 0M f)
                  []
                  (fraction-to-base f base))))))))

(defn to-base-seq
  "Convert decimal value to other base representation
  as a pair of sequences: a seq of values for the 
  integer and a seq of values for the fraction. Note
  that the fraction sequence may be an infinite
  sequence."
  [d base]
  (let [d (bigdec d)
        base (long base)
        {i ::integer, f ::fraction} (split'  d)]
    [(int-to-base i base) (fraction-to-base f base)]))

(defn to-base
  "Convert decimal value to other base representation."
  ([d base]
   (to-base d base {}))
  ([d base opts]
   (let [ms (or (::max-scale opts) 10)
         rs (or (::radix-separator opts) \.)
         mapval (->> (or (::digit-mapping opts)
                         "0123456789abcdef")
                     (partial nth))
         [i f] (to-base-seq d base)]
     (->> [(map mapval i) [rs] (map mapval (take ms f))]
          (apply concat)
          (string/join "")))))

;;
;; convenience fns
;;

(defn to-base2
  "Convert decimal to binary string representation.
  Second form takes options map. See to-base for details."
  ([d] (to-base2 d {}))
  ([d opts] (to-base d 2 (assoc opts ::digit-mapping fmt/base2))))

(defn to-base8
  "Convert decimal to octal string representation.
  Second form takes options map. See to-base for details."
  ([d] (to-base8 d {}))
  ([d opts] (to-base d 8 (assoc opts ::digit-mapping fmt/base8))))

(defn to-base16
  "Convert decimal to hexadecimal string representation.
  Second form takes options map. See to-base for details."
  ([d] (to-base16 d {}))
  ([d opts] (to-base d 16 (assoc opts ::digit-mapping fmt/base16))))

(defn to-base32
  "Convert decimal to base 32 string representation.

  See https://tools.ietf.org/html/rfc4648#page-8
  While RFC details encoding process for binary data,
  only the alphabet is used here."
  ([d] (to-base32 d {}))
  ([d opts] (to-base d 32 (assoc opts ::digit-mapping fmt/base32))))
