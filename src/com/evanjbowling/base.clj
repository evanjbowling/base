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
  (c/rational-to-base d base))

(defn to-base
  "Convert decimal value to other base representation."
  ([d base]
   (to-base d base {}))
  ([d base opts]
   (c/position-str d base opts)))

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

(defn to-base32hex
  ([d] (to-base32hex d {}))
  ([d opts] (to-base d 32 (assoc opts ::digit-mapping fmt/base32hex))))

(defn to-base36
  ([d] (to-base36 d {}))
  ([d opts] (to-base d 36 (assoc opts ::digit-mapping fmt/base36))))

(defn to-base58
  ([d] (to-base58 d {}))
  ([d opts] (to-base d 58 (assoc opts ::digit-mapping fmt/base58))))

(defn to-base64
  ([d] (to-base64 d {}))
  ([d opts] (to-base d 64 (assoc opts ::digit-mapping fmt/base64))))

(defn to-base64url
  ([d] (to-base64url d {}))
  ([d opts] (to-base d 64 (assoc opts ::digit-mapping fmt/base64url))))

(defn hex-to-base64url
  "Convert hexadecimal string to base64url."
  [s]
  (-> (to-base64url (BigInteger. s 16))
      (string/replace  #".A₆₄" "")))

(defn uuid-to-base64url
  "Convert uuid to base64url."
  [u]
  (let [s (cond-> u
            (not (string? u)) str)]
    (hex-to-base64url (string/replace s #"-" ""))))
