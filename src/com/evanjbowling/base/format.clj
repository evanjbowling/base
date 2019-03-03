(ns com.evanjbowling.base.format
  (:require
   [clojure.string :as string]))

(def O-9 "0123456789")
(def a-z "abcdefghijklmnopqrstuvwxyz")
(def A-Z "ABCDEFGHIJKLMNOPQRSTUVWXYZ")

(def base2 "01")
(def base8 "01234567")
(def base16 (str O-9 "ABCDEF"))
(def base32 (str A-Z "234567"))
(def base32hex (subs (str O-9 A-Z) 0 32))
(def base36 (str O-9 A-Z))
(def base58 "No 0,I,O,l" "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz")
(def base64 (str A-Z a-z O-9 "+/"))
(def base64url (str A-Z a-z O-9 "-_"))

(defn digit-mapping
  "Return a string such that 0 value is mapped to
  first character, 1 is mapped to second and so on."
  [base]
  (let [default (str O-9 A-Z a-z "-_")]
    (cond
      (or (< base 2)
          (< 64 base)) (throw (ex-info "No digit mapping" {}))
      (< base 11) (subs O-9 0 base)
      (= base 58) base58
      (= base 64) base64url
      :default (subs default 0 base))))

(defn sub
  "Convert integer n to subscript string."
  [n]
  (let [subscript "₀₁₂₃₄₅₆₇₈₉"
        front (if (neg? n) [\-] [])
        n' (cond-> (int n) (neg? n) (* -1))]
    (loop [left n'
           res []]
      (if (= 0 left)
        (->> (concat front res)
             (string/join ""))
        (let [[left' s] ((juxt (partial quot left)
                               (partial rem left)) 10)]
          (recur left'
                 (cons (nth subscript s) res)))))))

