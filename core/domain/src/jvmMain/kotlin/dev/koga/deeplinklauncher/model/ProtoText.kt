package dev.koga.deeplinklauncher.model

private val PairRegex = Regex(pattern = "([^{\\s]+)\\s*:\\s*\"?([^\"\\s}]+)")

data class ProtoText(
    val name: String,
    val fields: Map<String, String>,
) {
    companion object {

        fun from(
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
    }
}
