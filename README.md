# guiabolso-to-googlesheets

Send Guia Bolso CSV backups to Google Sheets.


## Requirements

1. [JDK 17](https://jdk.java.net/17/)
2. [Gradle](https://gradle.org/install/) >= version 6.7.1.


## Run Tests

```console
gradle test
```


## Building

```console
gradle jar
```


## CSV Layout

Guia Bolso CSV backup should follow the template:

```
Banco,Conta,Data da transação,Transação,Valor da transação,Categoria,Comentário
Caixa Econômica Federal,***1 / **3 / *****415-8,2018-10-06,REM BASICA,0.0,Juros,
```

You can refer a complete example in the [file](sample_guiabolso.csv).



