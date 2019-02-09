(ns com.evanjbowling.base-test
  (:require
    [clojure.test          :refer [deftest testing is are]]
    [com.evanjbowling.base :as b]))

(deftest test-int-to-base*
  (testing "base 2"
    (are [i base expect] (= expect (#'b/int-to-base* i base))
      0 2 {0 0}
      1 2 {0 1}
      2 2 {1 1}
      3 2 {1 1}
      4 2 {2 1}
      11 2 {3 1}))
  (testing "base 16"
    (is (= {0 11} (#'b/int-to-base* 11 16)))
    (is (= {0 12} (#'b/int-to-base* 12 16)))
    (is (= {1 1} (#'b/int-to-base* 16 16)))
    (is (= {1 1} (#'b/int-to-base* 17 16)))
    (is (= {1 15} (#'b/int-to-base* 255 16)))
    (is (= {2 1} (#'b/int-to-base* 256 16)))))

(deftest test-int-to-base
  (is (= "0" (#'b/int-to-base 0 16)))
  (is (= "b" (#'b/int-to-base 11 16)))
  (is (= "f" (#'b/int-to-base 15 16)))
  (is (= "10" (#'b/int-to-base 16 16)))
  (is (= "102" (#'b/int-to-base 258 16)))
  #_(testing "base less than 2 throws"
      (is (thrown? AssertionError (#'b/int-to-base 13 -1)))
      (is (thrown? AssertionError (#'b/int-to-base 13 0)))
      (is (thrown? AssertionError (#'b/int-to-base 13 1)))))

(deftest test-fraction-to-base
  (is (= {:value "0"
          :remainder 0M} (#'b/fraction-to-base 0.0M 2)))
  (is (= {:value "01"
          :remainder 0M} (#'b/fraction-to-base 0.25M 2)))
  (is (= {:value "4"
          :remainder 0M} (#'b/fraction-to-base 0.25M 16)))
  (is (= {:value "1999999999"
          :remainder 0.6M} (#'b/fraction-to-base 0.1M 16))))

(deftest test-to-base
  (testing "base 2"
    (is (= "100000000000000000000.0"
           (b/to-base 1048576M 2)))))
