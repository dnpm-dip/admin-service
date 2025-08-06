# Changelog

## 1.0.0 (2025-08-06)


### Features

* Refactored connection status info ([d6fa06c](https://github.com/dnpm-dip/admin-service/commit/d6fa06cdfeaa0966c0404a3213616b83433730e4))


### Bug Fixes

* Adapted scalac linting and fixed many reported errors (mostly unused imports) ([e25b7b1](https://github.com/dnpm-dip/admin-service/commit/e25b7b12a0c349d6bf1df4d008f6d7973facfdee))
* Adapted to refactored HttpConnector ([703cdf3](https://github.com/dnpm-dip/admin-service/commit/703cdf3d1f862d8b5a5a35ec58f52a3509904cd8))
* Added alphabetical ordering of sites in ConnectionReport ([04eca91](https://github.com/dnpm-dip/admin-service/commit/04eca91eccbe13818ddfaf1f5b64c58a14fe6e0a))
* Fixed baseURI of Connector to ensure properly formatted URIs ([d3bd934](https://github.com/dnpm-dip/admin-service/commit/d3bd93409a343a5c73aa88f640de2934e49e75bf))
* Minor improvement to log messages ([0b1e132](https://github.com/dnpm-dip/admin-service/commit/0b1e132e9f3069ce1fe18809858e2af8a94e9170))
* Refactored service implementation to fetch ConnectionReport on demand instead of via periodically scheduled task ([6b440ea](https://github.com/dnpm-dip/admin-service/commit/6b440eac7be302be1775fb6723b38072879525ee))
* Upgraded to Scala 2.13.16 ([b4f126a](https://github.com/dnpm-dip/admin-service/commit/b4f126a8d9fd6d30e96798579231aa6f52c092e1))
