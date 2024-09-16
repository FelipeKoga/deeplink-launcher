package dev.koga.deeplinklauncher.devicebridge.xcrun

import org.junit.Assert.assertEquals
import org.junit.Test

class XcrunParserTest {

    @Test
    fun `should parse xcrun input stream correctly`() {
        val input = """
            {
              "devices" : {
                "com.apple.CoreSimulator.SimRuntime.iOS-17-5" : [
                  {
                    "udid" : "BCBC21E3-0F5E-4D3D-9ED0-757C65C3F23E",
                    "state" : "Shutdown",
                    "name" : "iPhone 15 Pro"
                  },
                  {
                    "udid" : "C6FC62B7-6854-4A59-AF2B-D4B25E10E3F7",
                    "state" : "Booted",
                    "name" : "iPhone 15"
                  }
                ]
              }
            }
        """.trimIndent()

        val inputStream = input.byteInputStream()

        val devices = XcrunParser.parse(inputStream)

        assertEquals(
            listOf(
                XcrunDevice(
                    udid = "BCBC21E3-0F5E-4D3D-9ED0-757C65C3F23E",
                    state = "Shutdown",
                    name = "iPhone 15 Pro",
                ),
                XcrunDevice(
                    udid = "C6FC62B7-6854-4A59-AF2B-D4B25E10E3F7",
                    state = "Booted",
                    name = "iPhone 15",
                ),
            ),
            devices,
        )
    }
}
