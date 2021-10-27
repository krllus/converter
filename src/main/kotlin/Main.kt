import java.io.File

fun main(args: Array<String>) {

    val lines = if (args.size == 1) {
        val path = args[0]
        println("caminho do arquivo lido: $path")
        File(path).useLines { it.toList() }
    } else {
        println("digite conteudo no formato: nomeJSON,nomeCampoNoBancoDados,tipoCampo,tamanhoCampo,escala,ehChavePrimaria,ehCampoObrigatorio")
        val input = generateSequence(::readLine)
        input.toList()
    }

    lines.forEach { line ->
        val field = convertLineToFieldOrNull(line)

        if (field != null) {
            println(field)
            println()
        }
    }
}

private fun convertLineToFieldOrNull(line: String?): Field? {

    if (line.isNullOrEmpty()) return null

    val tokens = line.split(",")

    if (tokens.size != 7) {
        println("esperando linha no formato: nomeJSON,nomeCampoNoBancoDados,tipoCampo,tamanhoCampo,escala,ehChavePrimaria,ehCampoObrigatorio")
        println("obtido ${tokens.size}")
        return null
    }

    val name = tokens[0]
    val fieldName = tokens[1]
    val required = tokens[6].equals("SIM", ignoreCase = true)

    val fieldType = tokens[2]
    val fieldSize = tokens[3].toIntOrNull() ?: 0

    val type = getTypeFromToken(fieldType, fieldSize, fieldName)

    return Field(name, fieldName, required, type, line)
}

private fun getTypeFromToken(fieldType: String, fieldSize: Int, fieldName: String): Type {

    if (fieldSize == 0) {
        return Type.MANUAL
    }

    return when (fieldType) {
        "varchar" -> Type.STRING
        "int", "smallint" -> Type.INTEGER
        "datetime" -> Type.DATE
        "money", "decimal" -> Type.BIG_DECIMAL
        "char" -> {
            if (fieldSize == 1 || fieldSize == 2) {
                Type.ENUM(fieldName)
            } else {
                Type.STRING
            }
        }
        else -> Type.STRING
    }
}