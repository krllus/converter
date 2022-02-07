data class Field(
    val name: String,
    val fieldName: String,
    val required: Boolean = false,
    val fieldLength: Int,
    val scale: Int,
    val type: Type,
    val line: String,
    val isId: Boolean = false
) {
    override fun toString(): String {

        if (type == Type.MANUAL) {
            return "// FIXME não foi possível converter campo, conversão manual necessária\n// $line"
        }

        val idText = when (isId) {
            true -> "@Id"
            else -> ""
        }

        val enumeratedText = when (type) {
            is Type.ENUM -> "// TODO verificar linha\n// $line"
            else -> "\n"
        }

        val integer = fieldLength - scale

        val sizeText = when (type) {
            is Type.STRING -> "@Size(max=$fieldLength)"
            is Type.BIG_DECIMAL -> "@Digits(integer=$integer, fraction=$scale)"
            else -> "\n"
        }

        val requiredText = when (required) {
            true -> ", nullable = false)\n@NotNull(message = \"$name não pode ser nulo\")"
            else -> ")"
        }

        val result = mutableListOf<String>()
        result.add(idText)
        result.add(enumeratedText)
        result.add("@JsonProperty(\"$name\")")
        result.add(sizeText)
        result.add("@Column(name = \"$fieldName\", length=$fieldLength $requiredText")
        result.add("private ${type.descricao} $name;")

        return result.filterNot { it.isBlank() }.joinToString (separator = "\n")
    }
}

sealed class Type(val descricao: String) {
    object STRING : Type("String")
    object INTEGER : Type("Integer")
    object DATE : Type("LocalDateTime")
    object TIME : Type("LocalTime")
    object BIG_DECIMAL : Type("BigDecimal")
    object MANUAL : Type("MANUAL")
    data class ENUM(val value: String) : Type(value)
}