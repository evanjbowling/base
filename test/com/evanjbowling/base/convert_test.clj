(ns com.evanjbowling.base.convert-test
  (:require
   [clojure.test :refer [deftest testing is are]]
   [com.evanjbowling.base.convert :as c]))

(deftest test-int-to-base*
  (testing "base 2"
    (are [i base expect] (= expect (#'c/int-to-base* i base))
      0 2 {0 0}
      1 2 {0 1}
      2 2 {1 1}
      3 2 {1 1}
      4 2 {2 1}
      11 2 {3 1}))
  (testing "base 16"
    (is (= {0 11} (#'c/int-to-base* 11 16)))
    (is (= {0 12} (#'c/int-to-base* 12 16)))
    (is (= {1 1} (#'c/int-to-base* 16 16)))
    (is (= {1 1} (#'c/int-to-base* 17 16)))
    (is (= {1 15} (#'c/int-to-base* 255 16)))
    (is (= {2 1} (#'c/int-to-base* 256 16))))
  (testing "type handling"
    (is (= {0 11} (#'c/int-to-base* 11N 16)))
    (is (= {0 11} (#'c/int-to-base* 11 16M)))
    (is (= {0 11} (#'c/int-to-base* 11N 16N)))
    (is (= {15 8} (#'c/int-to-base* (+' Long/MAX_VALUE 1) 16)))))

(deftest test-int-to-base
  (is (= [0] (#'c/int-to-base 0 16)))
  (is (= [11] (#'c/int-to-base 11 16)))
  (is (= [15] (#'c/int-to-base 15 16)))
  (is (= [1 0] (#'c/int-to-base 16 16)))
  (is (= [1 0 2] (#'c/int-to-base 258 16)))
  #_(testing "base less than 2 throws"
      (is (thrown? AssertionError (#'c/int-to-base 13 -1)))
      (is (thrown? AssertionError (#'c/int-to-base 13 0)))
      (is (thrown? AssertionError (#'c/int-to-base 13 1)))))

(deftest test-fraction-to-base
  (is (= [0] (take 10 (#'c/fraction-to-base 0.0M 2))))
  (is (= [1] (take 10 (#'c/fraction-to-base 0.5M 2))))
  (is (= [0 1] (take 10 (#'c/fraction-to-base 0.25M 2))))
  (is (= [4] (take 10 (#'c/fraction-to-base 0.25M 16))))
  (is (= [1 9 9 9 9 9 9 9 9 9] (take 10 (#'c/fraction-to-base 0.1M 16)))))
