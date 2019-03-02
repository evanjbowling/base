(ns com.evanjbowling.base
  (:require
   [clojure.string                :as string]
   [com.evanjbowling.base.convert :as c]
   [com.evanjbowling.base.format  :as fmt]))

(defn to-base-seq
  "Convert decimal value to other base representation
  as a pair of sequences: a seq of values for the 
  integer and a seq of values for the fraction. Note
  that the fraction sequence may be an infinite
  sequence."
  [d base]
  (let [d (bigdec d)
        base (long base)
        {i ::integer, f ::fraction} (c/split'  d)]
    [(c/int-to-base i base) (c/fraction-to-base f base)]))

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
