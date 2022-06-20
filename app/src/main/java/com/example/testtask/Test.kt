package com.example.testtask

import com.google.gson.Gson
import java.io.File


fun main() {
    checkIfSynonyms("app\\example_big.in.json", "result.txt")
}

fun checkIfSynonyms(inputName: String, outputName: String) {
    val writer = File(outputName).bufferedWriter()
    val strBuilder = StringBuilder()
    for (i in 0 until File(inputName).readLines().size) {
        strBuilder.append(File(inputName).readLines()[i])
    }
    val jsonString: String = strBuilder.toString()
    val testCases = Gson().fromJson(jsonString, TestCases::class.java)
    testCases.testCases.forEach {
        it.queries.forEach { query ->
            val result = if (query[0].lowercase() == query[1].lowercase()) {
                true
            } else {
                recursion(
                    query[0].lowercase(),
                    query[1].lowercase(),
                    it,
                    query[0].lowercase(),
                    emptySet()
                )
            }
            if (result) {
                writer.write("synonyms")
                writer.newLine()
            } else {
                writer.write("different")
                writer.newLine()
            }
        }
    }
    writer.close()
}

fun recursion(
    firstWord: String,
    secondWord: String,
    testCase: TestCase,
    previous: String,
    levelList: Set<String>
): Boolean {
    val firstWordSynonyms = mutableSetOf<String>()
    testCase.dictionary.forEach { dictionary ->
        if (dictionary[0].lowercase() == firstWord.lowercase()) {
            firstWordSynonyms.add(dictionary[1].lowercase())
        }
        if (dictionary[1].lowercase() == firstWord.lowercase()) {
            firstWordSynonyms.add(dictionary[0].lowercase())
        }
        if ((dictionary[0].lowercase() == firstWord.lowercase() && dictionary[1].lowercase() == secondWord.lowercase()) ||
            (dictionary[0].lowercase() == secondWord.lowercase() && dictionary[1].lowercase() == firstWord.lowercase())
        ) {
            return true
        }
    }
    levelList.forEach { levelWord ->
        if (firstWordSynonyms.contains(levelWord.lowercase())) {
            firstWordSynonyms.remove(levelWord.lowercase())
        }
    }
    val bannedList = levelList + firstWordSynonyms
    if (firstWordSynonyms.size == 1 && firstWordSynonyms.contains(previous.lowercase())) return false
    else {
        var result = false
        firstWordSynonyms.forEach {
            if (it == secondWord.lowercase()) return true
            if (it != firstWord.lowercase() && it != previous.lowercase()) {

                result = result || recursion(
                    it,
                    secondWord,
                    testCase,
                    firstWord,
                    bannedList
                )
            }
        }
        return result
    }

}