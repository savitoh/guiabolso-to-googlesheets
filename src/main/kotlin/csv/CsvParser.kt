package csv

import java.io.File
import java.nio.file.Files

class CsvParser {

    private val headersPositionMapName = mapOf(
        0 to "Banco",
        1 to "Conta",
        2 to "Data da transação",
        3 to "Transação",
        4 to "Valor da transação",
        5 to "Categoria",
        6 to "Comentário"
    )

    data class CsvWrapper(
        val banco: String,
        val conta: String,
        val transactionAt: String,
        val transactionDescription: String,
        val transactionValue: String,
        val category: String,
        val comment: String
    )

    fun parse(file: File): Result<List<CsvWrapper>> {
        val rows = Files.readAllLines(file.toPath())
        return runCatching {
            return headerIsValid(rows).map {
                val rowsWithoutHeader = it.drop(1)
                rowsWithoutHeader.map { contentRow -> parse(contentRow).getOrThrow() }
            }
        }
    }

    private fun parse(contentRow: String): Result<CsvWrapper> {
        val columns = contentRow.split(",")
        return if (!columnsHasExpectedSize(columns))
            Result.failure(
                IllegalArgumentException("Row: \"${columns}\" doesn't have: ${headersPositionMapName.size} columns")
            )
        else
            Result.success(
                CsvWrapper(
                    banco = columns[0],
                    conta = columns[1],
                    transactionAt = columns[2],
                    transactionDescription = columns[3],
                    transactionValue = columns[4],
                    category = columns[5],
                    comment = columns[6]
                )
            )
    }

    private fun columnsHasExpectedSize(columns: List<String>) = headersPositionMapName.size == columns.size

    private fun headerIsValid(
        rows: List<String>,
        columnsHasExpectedSize: (List<String>) -> Boolean = ::columnsHasExpectedSize
    ): Result<List<String>> {
        val rowHeader = rows[0]
        val columnsHeader = rowHeader.split(",")
        return if (!columnsHasExpectedSize.invoke(columnsHeader))
            Result.failure(IllegalArgumentException("CSV doesn't have: ${headersPositionMapName.size} Headers"))
        else headersPositionMapName
            .filterNot { it.value == columnsHeader[it.key] }
            .map { it.value }
            .takeIf { it.isNotEmpty() }
            ?.let { headersNotFound ->
                Result.failure(
                    IllegalArgumentException(
                        headersNotFound.joinToString(
                            prefix = "CSV doesn't have these headers: ",
                            separator = ", "
                        )
                    )
                )
            } ?: Result.success(rows)
    }
}
