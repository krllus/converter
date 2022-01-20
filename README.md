# Converter
Conversor de `csv` para entidades `Java` 

## build
```
    gradle build
```

## execucao
```
    java -jar $projeto/builds/libs/converter-1.0.jar <nome_arquivo.csv>
```

### formatacao dados de entrada
```
valorJson, dadosExemplo, classeDestino, tabelaDestino, colunaDestino, tipoDados, comprimento, escala, ehChavePrimaria, obrigatorio
```

### exemplo saida
```
@JsonProperty("nomeCampo")
@Column(name = "nome_campo", nullable = false)
@NotNull("nomeCampo não pode ser nulo")
private String nomeCampo;  
```

