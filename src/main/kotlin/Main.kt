import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.io.File
import java.math.BigInteger
import java.nio.file.Files
import java.security.MessageDigest
import java.util.concurrent.Callable
import kotlin.system.exitProcess


@Command(
    name = "checksum",
    mixinStandardHelpOptions = true,
    version = ["Checksum CLI 0.1"],
    description = ["Prints the checksum (MD5 by default) of a file to STDOUT."]
)
class Checksum : Callable<Int> {

    @Parameters(index = "0", description = ["The file whose checksum to calculate."], split = ",")
    lateinit var files: List<File>

    private var algorithm = "MD5"

    override fun call(): Int {
        files.forEach {
            val fileContents = Files.readAllBytes(it.toPath())
            val digest = MessageDigest.getInstance(algorithm).digest(fileContents)
            println(("%0" + digest.size * 2 + "x").format(BigInteger(1, digest)))
        }
        return 0
    }
}

fun main(args: Array<String>): Unit = exitProcess(CommandLine(Checksum()).execute(*args))