# itertools-kotlin in Kotlin

[![GitHub link](https://img.shields.io/badge/GitHub-KotlinMania%2Fitertools--kotlin-blue.svg)](https://github.com/KotlinMania/itertools-kotlin)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.kotlinmania/itertools-kotlin)](https://central.sonatype.com/artifact/io.github.kotlinmania/itertools-kotlin)
[![Build status](https://img.shields.io/github/actions/workflow/status/KotlinMania/itertools-kotlin/ci.yml?branch=main)](https://github.com/KotlinMania/itertools-kotlin/actions)

This is a Kotlin Multiplatform line-by-line transliteration port of [`rust-itertools/itertools`](https://github.com/rust-itertools/itertools).

**Original Project:** This port is based on [`rust-itertools/itertools`](https://github.com/rust-itertools/itertools). All design credit and project intent belong to the upstream authors; this repository is a faithful port to Kotlin Multiplatform with no behavioural changes intended.

### Porting status

This is an **in-progress port**. The goal is feature parity with the upstream Rust crate while providing a native Kotlin Multiplatform API. Every Kotlin file carries a `// port-lint: source <path>` header naming its upstream Rust counterpart so the AST-distance tool can track provenance.

---

## Upstream README — `rust-itertools/itertools`

> The text below is reproduced and lightly edited from [`https://github.com/rust-itertools/itertools`](https://github.com/rust-itertools/itertools). It is the upstream project's own description and remains under the upstream authors' authorship; links have been rewritten to absolute upstream URLs so they continue to resolve from this repository.

## Itertools

Extra iterator adaptors, functions and macros.

Please read the [API documentation here](https://docs.rs/itertools/).

How to use with Cargo:

```toml
[dependencies]
itertools = "0.15.0"
```

How to use in your crate:

```rust
use itertools::Itertools;
```

## How to contribute
If you're not sure what to work on, try checking the [help wanted](https://github.com/rust-itertools/itertools/issues?q=is%3Aopen+is%3Aissue+label%3A%22help+wanted%22) label.

See our [CONTRIBUTING.md](https://github.com/rust-itertools/itertools/blob/master/CONTRIBUTING.md) for a detailed guide.

## License

Dual-licensed to be compatible with the Rust project.

Licensed under the Apache License, Version 2.0
https://www.apache.org/licenses/LICENSE-2.0 or the MIT license
https://opensource.org/licenses/MIT, at your
option. This file may not be copied, modified, or distributed
except according to those terms.

---

## About this Kotlin port

### Installation

```kotlin
dependencies {
    implementation("io.github.kotlinmania:itertools-kotlin:0.1.0")
}
```

### Building

```bash
./gradlew build
./gradlew test
```

### Targets

- macOS arm64
- Linux x64
- Windows mingw-x64
- iOS arm64 / simulator-arm64 (Swift export + XCFramework)
- JS (browser + Node.js)
- Wasm-JS (browser + Node.js)
- Android (API 24+)

### Porting guidelines

See [AGENTS.md](AGENTS.md) and [CLAUDE.md](CLAUDE.md) for translator discipline, port-lint header convention, and Rust → Kotlin idiom mapping.

### License

This Kotlin port is distributed under the same MIT license as the upstream [`rust-itertools/itertools`](https://github.com/rust-itertools/itertools). See [LICENSE](LICENSE) (and any sibling `LICENSE-*` / `NOTICE` files mirrored from upstream) for the full text.

Original work copyrighted by the itertools authors.  
Kotlin port: Copyright (c) 2026 Sydney Renee and The Solace Project.

### Acknowledgments

Thanks to the [`rust-itertools/itertools`](https://github.com/rust-itertools/itertools) maintainers and contributors for the original Rust implementation. This port reproduces their work in Kotlin Multiplatform; bug reports about upstream design or behavior should go to the upstream repository.
