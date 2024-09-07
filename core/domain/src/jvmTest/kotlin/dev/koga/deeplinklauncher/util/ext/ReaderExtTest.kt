package dev.koga.deeplinklauncher.util.ext

import dev.koga.deeplinklauncher.model.ProtoText
import org.junit.Assert.assertEquals
import org.junit.Test

class ReaderExtTest {

    @Test
    fun testUseProtoText() {
        val target = """
                device {
                    serial: "ZF523HKK7K"
                    state: DEVICE
                    connection_type: USB
                }
        """.trimIndent()

        val reader = target.reader()

        var protoText: ProtoText? = null

        reader.useProtoText("device") {
            protoText = it
        }

        assertEquals(
            ProtoText(
                name = "device",
                fields = mapOf(
                    "serial" to "ZF523HKK7K",
                    "state" to "DEVICE",
                    "connection_type" to "USB",
                ),
            ),
            protoText,
        )
    }
}
