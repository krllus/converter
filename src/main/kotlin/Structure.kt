data class Field(
    val name: String,
    val fieldName: String,
    val required: Boolean = false,
    val type: Type,
    val line: String
) {
    override fun toString(): String {

        val enumeratedText = when (type) {
            is Type.ENUM -> "// TODO verify line \n // $line \n @Enumerated(EnumType.STRING)"
            is Type.DATE -> "@Temporal(TemporalType.TIME)"
            else -> ""
        }

        if (type == Type.MANUAL) {
            return """
                // FIXME nao foi possivel converter campo, conversao manual necessaria
                // $line                
                """.trimIndent()
        }

        return """
            $enumeratedText
            @JsonProperty("$name")
            @Column(name = "$fieldName", nullable = ${!required})
            private ${type.descricao} $name;            
        """.trimIndent()
    }
}

sealed class Type(val descricao: String) {
    object STRING : Type("String")
    object INTEGER : Type("Integer")
    object DATE : Type("Date")
    object BIG_DECIMAL : Type("BigDecimal")
    object MANUAL : Type("MANUAL")
    data class ENUM(val value: String) : Type(value) {}
}