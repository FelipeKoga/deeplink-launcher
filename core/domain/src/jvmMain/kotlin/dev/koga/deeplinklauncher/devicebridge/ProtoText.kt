package dev.koga.deeplinklauncher.devicebridge


data class ProtoText(
    val name: String,
    val fields: Map<String, String>,
) {
    companion object {

        fun fromAdb(
            name: String,
            text: String,
            regex: Regex = Regex(pattern = "([^{\\s]+)\\s*:\\s*\"?([^\"\\s}]+)"),
        ): ProtoText {
            val pairs = mutableMapOf<String, String>()

            regex.findAll(text).forEach {
                val (key, value) = it.destructured

                pairs[key] = value
            }

            return ProtoText(
                name = name,
                fields = pairs,
            )
        }

        fun fromXcrun(
            name: String,
            udid: String,
        ): ProtoText {
            return ProtoText(
                name = name,
                fields = mapOf(
                    "udid" to udid
                ),
            )
        }
    }
}
