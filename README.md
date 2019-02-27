# base
[![Build Status](https://travis-ci.org/evanjbowling/base.svg?branch=master)](https://travis-ci.org/evanjbowling/base)
[![Clojars Project](https://img.shields.io/clojars/v/com.evanjbowling/base.svg)](https://clojars.org/com.evanjbowling/base)

A tiny Clojure library for converting decimals between different base representations.

## Installation

via Leiningen:

```
[com.evanjbowling/base "0.1.0-SNAPSHOT"]
```

## Usage

```clojure
(require '[com.evanjbowling.base :as b])
(b/to-base 16.25M 2)
;=> "10000.01"
(b/to-base 16.25M 16)
;=> "10.4"
```

## TODO

* convert fractional part as lazy sequence
* distinguish between `to-base` and `to-base-str` fns
* create options map with formatting options and optional mapping set to support bases greater than 16

## License

Copyright © 2019 Evan Bowling

Distributed under the Eclipse Public License version 1.0.
