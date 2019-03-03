# base
[![Build Status](https://travis-ci.org/evanjbowling/base.svg?branch=master)](https://travis-ci.org/evanjbowling/base)
[![Clojars Project](https://img.shields.io/clojars/v/com.evanjbowling/base.svg)](https://clojars.org/com.evanjbowling/base)

A tiny Clojure library for converting decimals to different base representations.

## Install

via Leiningen:

```
[com.evanjbowling/base "0.1.0-SNAPSHOT"]
```

## Use

```clojure
(require '[com.evanjbowling.base :as b])
```

There are two main functions: `to-base` and `to-base-seq`. The first produces a string representation while the second exposes the values as a sequence to support rendering in other formats (e.g. HTML).

Convert simple decimals:

```clojure
(b/to-base 16.25M 2)  ; "10000.01₂"
(b/to-base 16.25M 16) ; "10.4₁₆"
```

Convert larger decimals:

```clojure
(b/to-base 134983783748.98374983798374M 64) ; "B9tqRVE.-9cHhZXg1G₆₄"
(b/to-base 8561238652.018972M 37)           ; "3CH1F0G.0PZaLKFZT7₃₇"
```

Custom digit mapping:

```clojure
(b/to-base 16.25M 2 {:b/digit-mapping "XO"}) ; "OXXXX.XO₂"
```

## TODO

* review rational support
* document and expand options map with formatting options (add sequence format)
* repeating fractional sequence detection

## License

Copyright © 2019 Evan Bowling

Distributed under the Eclipse Public License version 1.0.
