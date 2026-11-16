import Testing
import Itertools

// Smoke test for the Kotlin → Swift Export → SPM → swift test pipeline.
//
// The file's mere existence and successful compilation prove three layers
// of the pipeline:
//
//   1. `embedSwiftExportForXcode` produced `Itertools.swiftmodule/`
//      and the supporting KotlinRuntimeSupport / ExportedKotlinPackages /
//      KotlinRuntime swiftmodule bundles. If any of them were missing,
//      `import Itertools` above would fail at compile time.
//
//   2. The static archive `libItertools.a` (produced by the
//      `linkSwiftExportBinaryDebugStaticMacosArm64` and
//      `mergeMacosDebugSwiftExportLibraries` tasks) supplied every
//      `__root____*` and `KotlinError`-related symbol the Swift modules
//      reference. If the archive were missing or empty, this test
//      executable would fail to link with "undefined symbols for
//      architecture arm64".
//
//   3. The Kotlin `swiftExport { moduleName = "Itertools" }` and
//      `flattenPackage = "io.github.kotlinmania.itertools"` configuration in
//      build.gradle.kts produced a module name that's both syntactically
//      valid as a Swift identifier and reachable from this Package.swift
//      via the `ItertoolsLibrary` product.
//
// Add more meaningful per-API tests below as the Swift Export surface
// grows. For now the import + a single passing assertion is the
// canary that the pipeline is green for this repo.
@Suite("Itertools Swift Export Tests")
struct ItertoolsExportTests {
    @Test("SizeHint arithmetic and addThenDiv work through Swift Export")
    func testSizeHintAndAddThenDiv() {
        let left = SizeHint(lower: 2, upper: 5)
        let right = SizeHint(lower: 3, upper: 7)
        let combined = add(a: left, b: right)
        #expect(combined.lower == 5)
        #expect(combined.upper == 12)

        let unbounded = SizeHint(lower: 4, upper: nil)
        let shifted = addScalar(sh: unbounded, x: 3)
        #expect(shifted.lower == 7)
        #expect(shifted.upper == nil)

        #expect(addThenDiv(n: 7, a: 2, d: 3) == 3)
        #expect(addThenDiv(n: 7, a: 2, d: 0) == nil)
    }
}
