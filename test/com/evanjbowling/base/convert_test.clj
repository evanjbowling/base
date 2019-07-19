(ns com.evanjbowling.base.convert-test
  (:require
   [clojure.test :refer [deftest testing is are]]
   [com.evanjbowling.base.convert :as c]))

(deftest test-nd
  (are [x expected]
       (= expected (#'c/nd x))
    1                     [1 1]
    1/3                   [1 3]
    -1/7                  [-1 7]
    1.1M                  [11 10]
    (+' 1 Long/MAX_VALUE) [9223372036854775808N 1]))

(deftest test-max-exponent-index
  (are [i b expected]
       (= expected (c/max-exponent-index i b))
    12345 10 4
    1234 10 3
    123 10 2
    12 10 1
    7 10 0
    7  7 1
    7  2 2))

(deftest test-int-seq
  (are [i b expected]
       (= expected (c/int-seq i b))
    7  2 [1 1 1]
    32 2 [1 0 0 0 0 0]
    3  3 [1 0]
    2  3 [2]))

(deftest test-fraction-seq
  (are [f b expected]
       (= expected (c/fraction-seq f b))
    1/3  3 [1]
    1/2  2 [1]
    1/8  2 [0 0 1]
    1/2 10 [5]))
