package csv

import csv.CsvParser.CsvWrapper
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Nested
import java.io.File
import kotlin.test.Test

class CsvParserTest {

    private val csvParser = CsvParser()

    @Nested
    inner class CsvCorrectFormatted {
        @Test
        fun `when csv has expected format then return parsed CSV`() {
            val path = "/csv/sample_guiabolso.csv"
            val file = getFromResources(path)


            val result: Result<List<CsvWrapper>> = csvParser.parse(file)


            result.isSuccess shouldBe true
            val csvWrappers = result.getOrDefault(listOf())
            csvWrappers shouldHaveSize 7
            csvWrappers.shouldContainExactly(
                CsvWrapper(
                    banco = "Caixa Econômica Federal",
                    conta = "***4 / **2 / *****425-6",
                    transactionAt = "2018-12-09",
                    transactionDescription = "REM BASICA",
                    transactionValue = "0.0",
                    category = "Juros"
                ),
                CsvWrapper(
                    banco = "Caixa Econômica Federal",
                    conta = "***4 / **2 / *****425-6",
                    transactionAt = "2018-10-08",
                    transactionDescription = "CRED JUROS",
                    transactionValue = "16.06",
                    category = "Rendimento"
                ),
                CsvWrapper(
                    banco = "Caixa Econômica Federal",
                    conta = "***4 / **2 / *****425-6",
                    transactionAt = "2018-10-08",
                    transactionDescription = "DP DIN LOT",
                    transactionValue = "520.0",
                    category = "Outras rendas"
                ),
                CsvWrapper(
                    banco = "Banco Itaú Unibanco S.A.",
                    conta = "Banco Itaú Unibanco S.A.",
                    transactionAt = "2018-10-08",
                    transactionDescription = "SAQUE 24H 11586773 06/10",
                    transactionValue = "-450.0",
                    category = "Moradia"
                ),
                CsvWrapper(
                    banco = "Banco Itaú Unibanco S.A.",
                    conta = "Banco Itaú Unibanco S.A.",
                    transactionAt = "2018-10-08",
                    transactionDescription = "INT PAG TIT BANCO 422",
                    transactionValue = "-164.0",
                    category = "Pagamento de cartão"
                ),
                CsvWrapper(
                    banco = "Banco Itaú Unibanco S.A.",
                    conta = "Banco Itaú Unibanco S.A.",
                    transactionAt = "2018-11-30",
                    transactionDescription = "REMUN/SALARIO",
                    transactionValue = "2125.51",
                    category = "Remuneração"
                ),
                CsvWrapper(
                    banco = "Banco Itaú Unibanco S.A.",
                    conta = "Banco Itaú Unibanco S.A.",
                    transactionAt = "2018-12-31",
                    transactionDescription = "EST LANCAMENTO DE DEBITO",
                    transactionValue = "2500.0",
                    category = "Outras rendas"
                )
            )
        }
    }

    @Nested
    inner class CsvBadFormatted {

        @Test
        fun `when csv has less than 7 headers then return error`() = testCase(
            path = "/csv/less-than-7-headers.csv", messageExpected = "CSV doesn't have: 7 Headers"
        )


        @Test
        fun `when csv has more than 7 headers then return error`() = testCase(
            path = "/csv/more-than-7-headers.csv", messageExpected = "CSV doesn't have: 7 Headers"
        )

        @Test
        fun `when csv has unexpectedHeader headers then return error`() = testCase(
            path = "/csv/unexpected-headers.csv", messageExpected = "CSV doesn't have these headers: Transação, Comentário"
        )

        @Test
        fun `when csv has any rows with column size less than 7 then return error`() = testCase(
            path = "/csv/row-less-than-7-columns.csv",
            messageExpected = "Row: \"[Banco Itaú Unibanco S.A., 2018-10-08, INT PAG TIT BANCO 422, -164.0, Pagamento de cartão, ]\" doesn't have: 7 columns"
        )

        @Test
        fun `when csv has any rows with column size more than 7 then return error`() = testCase(
            path = "/csv/row-more-than-7-columns.csv",
            messageExpected = "Row: \"[Banco Itaú Unibanco S.A., Banco Itaú Unibanco S.A., 2018-10-08, INT PAG TIT BANCO 422, -164.0, Pagamento de cartão, Extra Column, ]\" doesn't have: 7 columns"
        )
    }

    private fun testCase(path: String, messageExpected: String) {
        val file = getFromResources(path)


        val result: Result<List<CsvWrapper>> = csvParser.parse(file)


        result.isFailure shouldBe true
        result.exceptionOrNull()?.message shouldBe messageExpected
    }

    private fun getFromResources(path: String): File =
        object {}.javaClass.getResource(path)?.file?.let { File(it) }
            ?: throw RuntimeException("Cannot open the file: $path")

}