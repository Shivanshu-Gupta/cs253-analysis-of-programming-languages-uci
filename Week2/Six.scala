//package Week2

import scala.collection.mutable
import scala.io.Source

object Six {
  def main(args: Array[String]): Unit = {
    printall(sort(frequencies(removeStopWords(scan(
      filterCharsAndNormalize(readFile(args(0))))))).slice(0, 25))
  }

  def readFile(pathToFile: String): String = {
    val source = Source.fromFile(pathToFile)
    val data = source.getLines().mkString(" ")
    source.close()
    data
  }

  def filterCharsAndNormalize(data: String): String = {
    data.replaceAll("[\\W_]+", " ").toLowerCase
  }

  def scan(data: String): Array[String] = {
    data.split("\\s+")
  }

  def removeStopWords(words: Array[String]): Array[String] = {
    val stopWords: mutable.HashSet[String] = mutable.HashSet(('a' to 'z').map(_.toString): _*)
    val stopWordsSource = Source.fromFile("../stop_words.txt")
    stopWords.addAll(stopWordsSource.getLines().next().split(","))
    stopWordsSource.close()
    words.filter(!stopWords.contains(_))
  }

  def frequencies(words: Array[String]): mutable.Map[String, Int] = {
    val freqMap: mutable.Map[String, Int] = mutable.HashMap[String, Int]()
    for(word <- words) freqMap.addOne((word, freqMap.getOrElse(word, 0) + 1))
    freqMap
  }

  def sort(freqMap: mutable.Map[String, Int]): Seq[(String, Int)] = {
    freqMap.toSeq.sortBy(-_._2)
  }

  def printall(wordFreqs: Seq[(String, Int)]): Unit = {
    if(wordFreqs.nonEmpty) {
      println(s"${wordFreqs.head._1} - ${wordFreqs.head._2}")
      printall(wordFreqs.tail)
    }
  }
}

// mr - 786
// elizabeth - 635
// very - 488
// darcy - 418
// such - 395
// mrs - 343
// much - 329
// more - 327
// bennet - 323
// bingley - 306
// jane - 295
// miss - 283
// one - 275
// know - 239
// before - 229
// herself - 227
// though - 226
// well - 224
// never - 220
// sister - 218
// soon - 216
// think - 211
// now - 209
// time - 203
// good - 201