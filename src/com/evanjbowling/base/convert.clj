(ns com.evanjbowling.base.convert
  (:require
   [clojure.math.numeric-tower   :as math]
   [clojure.pprint               :as pprint]
   [clojure.string               :as string]
   [com.evanjbowling.base.format :as fmt]))

(defn ^:private nd
  "Returns [numerator denominator] for x.
  Handles rationals, integers and big decimals."
  [x]
  (if (integer? x) [x 1]
      (let [r (rationalize x)]
        (if (integer? r) [r 1]
            ((juxt numerator denominator) r)))))

(defn max-exponent-index
  "Max exponent index in base zero to represent
  integer i in base b."
  [i b]
  (let [i (cond-> i (neg? i) (*' -1))]
    (loop [e 1, v 1N]
      (if (< i (*' v b))
        (dec e)
        (recur (inc e) (*' v b))))))

(defn int-seq
  "Lazy sequence of values to represent integer i
  in base b ordered in big endian."
  ([i b]
   (lazy-seq (int-seq i b (max-exponent-index i b))))
  ([i b e]
   (if (< e 0)
     []
     (if (< i (math/expt b e))
       (cons 0 (lazy-seq (int-seq i b (dec e))))
       (loop [d (- b 1)]
         (let [dv (*' d (math/expt b e))]
           (if (<= dv i)
             (cons d
                   (lazy-seq (int-seq (-' i dv)
                                      b
                                      (dec e))))
             (recur (dec d)))))))))

(defn fraction-seq
  [f b]
  (lazy-seq
   (let [f' (*' f b)]
     (if-not (ratio? f')
       [f']
       (let [[n d] (nd f')]
         (cons (quot n d)
               (fraction-seq (/ (rem n d) d) b)))))))

(defn next-digit
  "Compute the next fractional digit and remaining
  fraction."
  [fraction b]
  (let [f' (*' fraction b)
        [n d] (nd f')]
    {:integer (quot n d)
     :remaining (/ (rem n d) d)}))

(defn split-prefix-repetend
  [values remaining]
  (let [repetend-start (:index (get values remaining))
        values' (->> (vals values)
                     (sort-by :index))]
    [(->> (filter #(< (:index %) repetend-start) values')
          (map :integer))
     (->> (remove #(< (:index %) repetend-start) values')
          (map :integer))]))

(defn new-fraction-seq
  "Returns [[prefix][repetend]]."
  [fraction b]
  (loop [values {}
         curr fraction]
    (let [{:keys [integer remaining]} (next-digit curr b)]
      (cond
        ;; sequence termination
        (zero? remaining)
        [(->> (into [] values)
              (sort-by :index)
              (map :integer)
              ((fn [is] (conj is integer))))
         []]

        ;; sequence repetition detected
        (contains? values remaining)
        (split-prefix-repetend values remaining)

        ;; otherwise, keep looping
        :else (recur (assoc values remaining
                            {:integer integer
                             :remaining remaining
                             :index (-> values keys count)})
                     remaining)))))

(defn rational-to-base
  [r b]
  (let [[n d] (nd r)]
    [(int-seq (quot n d) b)
     (fraction-seq (/ (rem n d) d) b)]))

(defn position-str
  [d base opts]
  (let [ms (or (:com.evanjbowling.base/max-scale opts) 10)
        rs (or (:com.evanjbowling.base/radix-separator opts) \.)
        bi (or (:com.evanjbowling.base/base-indicator opts)
               :suffix)
        mapval (->> (or (:com.evanjbowling.base/digit-mapping opts)
                        (fmt/digit-mapping base))
                    (partial nth))
        [i f] (rational-to-base d base)]
    (->> [(map mapval i)
          [rs]
          (map mapval (take ms f))
          (when (= :suffix bi) [(fmt/sub base)])]
         (remove nil?)
         (apply concat)
         (string/join ""))))

