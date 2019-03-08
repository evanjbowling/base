# base
[![Build Status](https://travis-ci.org/evanjbowling/base.svg?branch=master)](https://travis-ci.org/evanjbowling/base)
[![Dependencies Status](https://versions.deps.co/evanjbowling/base/status.svg)](https://versions.deps.co/evanjbowling/base)

[![Clojars Project](https://img.shields.io/clojars/v/com.evanjbowling/base.svg)](https://clojars.org/com.evanjbowling/base)

A tiny Clojure library for converting decimals to different base representations.

## Quick Start

```bash
clj -Sdeps '{:deps {com.evanjbowling/base {:mvn/version "0.1.0"}}}' \
    -e "(require '[com.evanjbowling.base :as b])(b/to-base 1234567.8901M 13)"
```
Output:
```bash
"342C19.B7571B8C9B₁₃"
```

## Install

Leiningen/Boot:

```clojure
[com.evanjbowling/base "0.1.0"]
```

Clojure CLI/deps.edn:

```clojure
com.evanjbowling/base {:mvn/version "0.1.0"}
```

## Use

Review the [Docs](https://evanjbowling.github.io/base/doc/).

```clojure
(require '[com.evanjbowling.base :as b])
```

There are two main functions:

* `to-base` - returns a string representation
* `to-base-seq` - returns a sequence of numeric values (For every value v, 0 <= v < base) with two parts: integer sequence and fractional sequence.

The sequence version is meant to be useful for exploring the whole sequence (the fractional sequence may be infinite) or for encoding the values in another format (e.g. HTML).

### String Representations

The `to-base` function maps every digit value in the sequence to the relevant character for the base.

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

* review convenience fns, add common name aliases
* proper docs
* review rational support
* document and expand options map with formatting options (add sequence format)
* repeating fractional sequence detection

## Dedicate

This library is dedicated to the second sentence of the second paragraph of sections 5.1 and 5.2 on page 11 of rfc6234.

## License

Copyright © 2019 Evan Bowling

Distributed under the Eclipse Public License version 1.0.
