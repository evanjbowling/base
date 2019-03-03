(ns com.evanjbowling.base.format-test
  (:require
   [clojure.test :refer [deftest testing are]]
   [com.evanjbowling.base.format :as f]))

(deftest test-digit-mapping
  (testing "default mappings for certain bases"
    (are [base mapping] (= mapping (f/digit-mapping base))
      2 "01"
      7 "0123456"
      16 "0123456789ABCDEF")))

(deftest test-sub
  (are [n expected] (= expected (f/sub n))
    2   "₂"
    3   "₃"
    8   "₈"
    10  "₁₀"
    16  "₁₆"
    128 "₁₂₈"
    -7  "-₇"))
