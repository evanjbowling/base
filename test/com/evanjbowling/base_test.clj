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
    (is (= {2 1} (#'b/int-to-base* 256 16))))
  (testing "type handling"
    (is (= {0 11} (#'b/int-to-base* 11N 16)))
    (is (= {0 11} (#'b/int-to-base* 11 16M)))
    (is (= {0 11} (#'b/int-to-base* 11N 16N)))
    (is (= {15 8} (#'b/int-to-base* (+' Long/MAX_VALUE 1) 16)))))

(deftest test-int-to-base
  (is (= [0] (#'b/int-to-base 0 16)))
  (is (= [11] (#'b/int-to-base 11 16)))
  (is (= [15] (#'b/int-to-base 15 16)))
  (is (= [1 0] (#'b/int-to-base 16 16)))
  (is (= [1 0 2] (#'b/int-to-base 258 16)))
  #_(testing "base less than 2 throws"
      (is (thrown? AssertionError (#'b/int-to-base 13 -1)))
      (is (thrown? AssertionError (#'b/int-to-base 13 0)))
      (is (thrown? AssertionError (#'b/int-to-base 13 1)))))

(deftest test-fraction-to-base
  (is (= [0] (take 10 (#'b/fraction-to-base 0.0M 2))))
  (is (= [1] (take 10 (#'b/fraction-to-base 0.5M 2))))
  (is (= [0 1] (take 10 (#'b/fraction-to-base 0.25M 2))))
  (is (= [4] (take 10 (#'b/fraction-to-base 0.25M 16))))
  (is (= [1 9 9 9 9 9 9 9 9 9] (take 10 (#'b/fraction-to-base 0.1M 16)))))

(deftest test-to-base
  (testing "base 2"
    (is (= "100000000000000000000.0"
           (b/to-base 1048576M 2)))))

(deftest test-input-handling
  (let [opts {::b/digit-mappings "0123456789abcdef"}]
    (are [d base expected] (= expected (b/to-base d base opts))
      1.25 16 "1.4"
      1.25 16M "1.4"
      1.25M 16M "1.4"
      (int 12) 16 "c.0"
      (long 12) 16 "c.0"
      (short 12) 16 "c.0")))

(deftest test-to-base-seq-hand-verified
  (are [d base expected]
       (= expected (b/to-base-seq d base))

    1.25M 2  [[1] [0 1]]
    1.25M 4  [[1] [1]]
    1.25M 8  [[1] [2]]
    1.25M 10 [[1] [2 5]]
    1.25M 12 [[1] [3]]
    1.25M 16 [[1] [4]]))

(deftest test-to-base-hand-verified
  (let [opts {::b/digit-mappings "0123456789abcdef"}]
    (are [d base expected] (= expected (b/to-base d base opts))
      1.25M 2  "1.01"
      1.25M 4  "1.1"
      1.25M 8  "1.2"
      1.25M 10 "1.25"
      1.25M 12 "1.3"
      1.25M 16 "1.4"
      1.25M 3  "1.0202020202"

      1.23456M 5  "1.10413"
      1.23456M 2  "1.0011110000"
      1.23456M 4  "1.0330003001"
      1.23456M 8  "1.1700603762"
      1.23456M 10 "1.23456"
      1.23456M 12 "1.2993a04a74"
      1.23456M 16 "1.3c0c1fc8f3"

      25.2M 2 "11001.0011001100"
      25.2M 4   "121.0303030303"
      25.2M 8    "31.1463146314"
      25.2M 12   "21.2497249724"
      25.2M 16   "19.3333333333"

      1.0M 2 "1.0"
      1.0M 3 "1.0"
      1.0M 4 "1.0"
      1.0M 5 "1.0"))
  (is (apply = (map (partial b/to-base 0.0M) (range 2 37)))
      "0.0₁₀=0.0₂=0.0₃=..."))

