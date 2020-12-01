//package Week2

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object Five {
  val data: ArrayBuffer[Char] = ArrayBuffer[Char]()
  val words: ArrayBuffer[String] = ArrayBuffer[String]()
  val freqMap: mutable.Map[String, Int] = mutable.HashMap[String, Int]()
  val wordFreqs: ArrayBuffer[(String, Int)] = ArrayBuffer[(String, Int)]()

  def main(args: Array[String]): Unit = {
    readFile(args(0))
    filterCharsAndNormalize()
    scan()
    removeStopWords()
    frequencies()
    sort()
    for(tf <- wordFreqs.slice(0, 25)) {
      println(s"${tf._1} - ${tf._2}")
    }
  }

  def readFile(pathToFile: String): Unit = {
    val source = Source.fromFile(pathToFile)
    data.appendAll(source.getLines().mkString(" "))
  }

  def filterCharsAndNormalize(): Unit = {
    for(i <- data.indices) {
      if(!data(i).isLetter) data(i) = ' '
      else data(i) = data(i).toLower
    }
  }

  def scan(): Unit = {
    val dataStr = data.mkString("")
    words.appendAll(dataStr.split("\\s+"))
  }

  def removeStopWords(): Unit = {
    val stopWordsSource = Source.fromFile("../stop_words.txt")
    val stopWords: mutable.HashSet[String] = mutable.HashSet[String]()
    stopWords.addAll(stopWordsSource.getLines().next().split(",").toSet)
    stopWords.addAll(('a' to 'z').map(_.toString))
    val indices = words.indices.filter(i => stopWords.contains(words(i)))
    for (i <- indices.reverse) words.remove(i)
  }

  def frequencies(): Unit = {
    for(word <- words) freqMap.addOne((word, freqMap.getOrElse(word, 0) + 1))
  }

  def sort(): Unit = {
    wordFreqs.appendAll(freqMap.toSeq.sortBy(-_._2))
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