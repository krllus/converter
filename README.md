# fujioka-converter
Conversor para ajudar com mapeamento de classes

## build do projeto
```
    gradle build
```

## execucao
```
    java -jar $projeto/builds/libs/fujioka-converter-1.0.jar <nome_arquivo.csv>
```

### formatacao dados de entrada
```
nomeJSON,nomeCampoNoBancoDados,tipoCampo,tamanhoCampo,escala,ehChavePrimaria,ehCampoObrigatorio
```

### exemplo saida
```
@JsonProperty("nomeCampo")
@Column(name = "nome_campo", nullable = false)
private String nomeCampo;  
```

