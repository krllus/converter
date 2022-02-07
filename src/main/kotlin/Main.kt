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
            println(field.toString())
            println()
        }
    }
}

private fun convertLineToFieldOrNull(line: String?): Field? {

    if (line.isNullOrEmpty()) return null

    val tokens = line.split(",")

    return when (tokens.size) {
        7 -> parseFor7Fields(line, tokens)
        10 -> parseFor10Fields(line, tokens)
        else -> {
            println("quantidade de tokens nao suportado, tente com 7 ou 10")
            null
        }
    }
}

private fun getTypeFromToken(fieldType: String, fieldSize: Int, fieldName: String): Type {

    if (fieldSize == 0) {
        return Type.MANUAL
    }

    return when (fieldType) {
        "varchar" -> Type.STRING
        "int", "smallint", "bigint" -> Type.INTEGER
        "datetime" -> Type.DATE
        "time" -> Type.TIME
        "money", "decimal", "numeric" -> Type.BIG_DECIMAL
        "char" -> {
            if (fieldSize == 1 || fieldSize == 2) {
                Type.ENUM(fieldName)
            } else {
                Type.STRING
            }
        }
        else -> Type.MANUAL
    }
}

private fun parseFor10Fields(line: String, tokens: List<String>): Field? {
    if (tokens.size != 10) {
        println("esperando linha no formato: json, dados_exemplo, classe_destino, tabela_destino, coluna_destino, tipo_dados,comprimento,escala,chave_primaria,obrigatorio")
        println("obtido ${tokens.size}")
        return null
    }

    val name = tokens[0]
    val fieldName = tokens[4]
    val fieldType = tokens[5]
    val fieldSize = tokens[6].toIntOrNull() ?: 0
    val scale = tokens[7].toIntOrNull() ?: 0

    val isId = tokens[8].equals("SIM", ignoreCase = true)
    val required = tokens[9].equals("SIM", ignoreCase = true)

    val type = getTypeFromToken(fieldType, fieldSize, fieldName)

    return Field(name, fieldName, required, fieldSize, scale, type, line, isId)
}

private fun parseFor7Fields(line: String, tokens: List<String>): Field? {
    if (tokens.size != 7) {
        println("esperando linha no formato: nomeJSON,nomeCampoNoBancoDados,tipoCampo,tamanhoCampo,escala,ehChavePrimaria,ehCampoObrigatorio")
        println("obtido ${tokens.size}")
        return null
    }

    val name = tokens[0]
    val fieldName = tokens[1]
    val isId = tokens[5].equals("SIM", ignoreCase = true)
    val required = tokens[6].equals("SIM", ignoreCase = true)

    val fieldType = tokens[2]
    val fieldSize = tokens[3].toIntOrNull() ?: 0

    val type = getTypeFromToken(fieldType, fieldSize, fieldName)

    return Field(name, fieldName, required, 0, 0, type, line, isId)
}