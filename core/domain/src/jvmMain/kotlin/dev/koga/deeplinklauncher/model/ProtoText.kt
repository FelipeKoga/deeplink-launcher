package dev.koga.deeplinklauncher.model

private val PairRegex = Regex(pattern = "([^{\\s]+)\\s*:\\s*\"?([^\"\\s}]+)")

data class ProtoText(
    val name: String,
    val fields: Map<String, String>,
) {
    companion object {

        fun fromAdb(
            name: String,
            text: String,
            regex: Regex = PairRegex,
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
                    "serial" to udid
                ),
            )
        }
    }
}
