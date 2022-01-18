data class Field(
    val name: String,
    val fieldName: String,
    val required: Boolean = false,
    val length: Int,
    val scale: Int,
    val type: Type,
    val line: String,
    val isId: Boolean = false
) {
    override fun toString(): String {

        val idText = when (isId) {
            true -> "@Id"
            else -> ""
        }

        val enumeratedText = when (type) {
            is Type.ENUM -> "// TODO verificar linha \n // $line"
            //is Type.DATE -> "@Temporal(TemporalType.TIMESTAMP)"
            else -> ""
        }

        if (type == Type.MANUAL) {
            return """
                // FIXME não foi possível converter campo, conversão manual necessária
                // $line                
                """.trimIndent()
        }

        val requiredText = when (required) {
            true -> ", nullable = false)\n@NotNull(message = \"$name não pode ser nulo\")"
            else -> ")"
        }

        val fieldLength = length - scale

        return """
            $idText
            $enumeratedText
            @JsonProperty("$name")
            @Size(max=$fieldLength)
            @Column(name = "$fieldName", length=$fieldLength $requiredText            
            private ${type.descricao} $name;            
        """.trimIndent()
    }
}

sealed class Type(val descricao: String) {
    object STRING : Type("String")
    object INTEGER : Type("Integer")
    object DATE : Type("LocalDateTime")
    object BIG_DECIMAL : Type("BigDecimal")
    object MANUAL : Type("MANUAL")
    data class ENUM(val value: String) : Type(value)
}