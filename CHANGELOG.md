# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [Upcoming] ;; 1.0.0
### Changed
- Changed `to-base` to return a sequence instead of a formatted string. The sequence returned is `[[integer values] [prefix values] [repetend values]]`. Sequences can be computed for a much larger range of input values and bases whereas formatted strings require a value-to-character mapping.
### Removed
- Removed `hex-to-base64url`, `uuid-to-base64url` and other convenience functions.

## [Unreleased] ;; 0.1.2
### Fixed
- #1 Support for rationals that cannot be coerced to BigDecimal (e.g. 1/3, 1/7, 1/9).
### Deprecated
- `hex-to-base64url`, `uuid-to-base64url` and other convenience functions.

## [0.1.1] - 2019-04-13
### Added
- Add `hex-to-base64url`, `uuid-to-base64url` and other convenience functions. 

## [0.1.0] - 2019-03-03
### Added
- Initial release.
- Add `to-base` and `to-base-seq` functions.


[Unreleased] https://github.com/evanjbowling/base/compare/0.1.0...HEAD
