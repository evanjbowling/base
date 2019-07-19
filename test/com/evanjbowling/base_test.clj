(ns com.evanjbowling.base-test
  (:require
   [clojure.test          :refer [deftest testing is are]]
   [com.evanjbowling.base :as b]))

(deftest test-to-base-seq-hand-verified
  (are [d base expected]
       (= expected (b/to-base-seq d base))

    1.25M 2  [[1] [0 1]]
    1.25M 4  [[1] [1]]
    1.25M 8  [[1] [2]]
    1.25M 10 [[1] [2 5]]
    1.25M 12 [[1] [3]]
    1.25M 16 [[1] [4]]))

(deftest test-to-base
  (testing "base 2"
    (is (= "100000000000000000000.0₂"
           (b/to-base 1048576M 2)
           (b/to-base2 1048576M))))
  (testing "repeating decimal rational support"
    (is (= "0.1₃" (b/to-base (/ 1 3) 3)))))

(deftest test-input-handling
  (are [d base expected] (= expected (b/to-base d base))
    1.25       16  "1.4₁₆"
    1.25       16M "1.4₁₆"
    1.25M      16M "1.4₁₆"
    (/ 5 4)    16M "1.4₁₆"
    (int 12)   16  "C.0₁₆"
    (long 12)  16  "C.0₁₆"
    (short 12) 16  "C.0₁₆"))

(deftest test-to-base-hand-verified
  (let [opts {::b/base-indicator :none}]
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
      1.23456M 12 "1.2993A04A74"
      1.23456M 16 "1.3C0C1FC8F3"

      25.2M 2 "11001.0011001100"
      25.2M 4   "121.0303030303"
      25.2M 8    "31.1463146314"
      25.2M 12   "21.2497249724"
      25.2M 16   "19.3333333333"

      1.0M 2 "1.0"
      1.0M 3 "1.0"
      1.0M 4 "1.0"
      1.0M 5 "1.0")
    (is (apply = (map #(b/to-base 0.0M % opts) (range 2 37)))
        "0.0₁₀=0.0₂=0.0₃=...")))

