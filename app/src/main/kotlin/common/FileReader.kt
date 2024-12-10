package common

class FileReader {

    /**
     * Deprecated, prefer [readFileLines]
     */
    @Deprecated("Prefer readFileLines, which is more clear")
    fun readFile(filename: String): List<String> {
        val input = this::class.java.getResourceAsStream("/$filename")
        val lines = input?.bufferedReader()?.readLines()

        return lines ?: emptyList()
    }

    fun readFileLines(filename: String): List<String> {
        return readFile(filename)
    }

    fun readFileText(filename: String): String {
        val input = this::class.java.getResourceAsStream("/$filename")
        val lines = input?.bufferedReader()?.readText()

        return lines ?: ""
    }
}
