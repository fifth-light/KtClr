package top.fifthlight.asmnet.binary

// PE Format § Optional Header Data Directories
data class DataDirectory(
    val rva: UInt,
    val size: UInt,
)
