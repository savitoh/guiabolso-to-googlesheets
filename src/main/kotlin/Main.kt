import csv.CsvParser
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.io.File
import java.util.concurrent.Callable
import kotlin.system.exitProcess


@Command(
    name = "guiabolso-to-googlesheets",
    mixinStandardHelpOptions = true,
    version = ["guiabolso-to-googlesheets CLI 0.1"],
    description = ["Send Guiabolso CSVs backups to Google Sheets."]
)
class Main : Callable<Int> {

    @Parameters(index = "0", description = ["Guiabolso CSVs whose send to Google Sheets."], split = ",")
    lateinit var files: List<File>

    override fun call(): Int {
        val csvParser = CsvParser()
        files.map {
            csvParser.parse(it)
                .onSuccess { csvWrapper ->
                    println(
                        csvWrapper.joinToString(
                            prefix = "**********************************CSV Wrappers********************************\n",
                            separator = "\n",
                            postfix = "\n*********************************END******************************************"
                        )
                    )
                }
                .onFailure { error -> println("Error: ${error.message} to parser File: ${it.name}") }
        }
        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(Main()).execute(*args))