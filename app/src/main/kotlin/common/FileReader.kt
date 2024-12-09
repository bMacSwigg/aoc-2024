package common

class FileReader {

    fun readFile(filename: String): List<String> {
        val input = this::class.java.getResourceAsStream("/$filename")
        val lines = input?.bufferedReader()?.readLines()

        return lines ?: emptyList()
    }
}
