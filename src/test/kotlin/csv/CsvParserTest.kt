package csv

import csv.CsvParser.CsvWrapper
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import java.io.File
import kotlin.test.Test
import kotlin.test.fail

class CsvParserTest {

    private lateinit var csvParser: CsvParser

    @BeforeEach
    fun setUp() {
        csvParser = CsvParser()
    }

    @Test
    fun `when csv has less than 7 headers then return error`() = testCase(
        path = "/csv/less-than-7-headers.csv", messageExpected = "CSV doesn't have: 7 Headers"
    )

    @Test
    fun `when csv has more than 7 headers then return error`() = testCase(
        path = "/csv/more-than-7-headers.csv", messageExpected = "CSV doesn't have: 7 Headers"
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

    private fun testCase(path: String, messageExpected: String) {
        getFromResources(path)?.let {

            val result: Result<List<CsvWrapper>> = csvParser.parse(it)


            result.isFailure shouldBe true
            messageExpected shouldBe result.exceptionOrNull()?.message
        } ?: fail("Cannot open the file: $path")
    }

    private fun getFromResources(path: String): File? = object {}.javaClass.getResource(path)?.file?.let { File(it) }

}