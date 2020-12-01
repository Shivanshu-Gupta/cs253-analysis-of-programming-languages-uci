//package Week3
// https://stackoverflow.com/questions/21317151/scala-passing-a-function-that-takes-another-function-as-a-parameter

import scala.collection.mutable
import scala.io.Source

object Nine {
  type noopType = None.type => Unit
  def noop(func: None.type): Unit = {}

  type printAllType = (Seq[(String, Int)], noopType) => Unit
  def printall(wordFreqs: Seq[(String, Int)], func: noopType): Unit = {
    for(tf <- wordFreqs.slice(0, 25)) println(s"${tf._1} - ${tf._2}")
    func(None)
  }

  type sortType = (mutable.Map[String, Int], printAllType) => Unit
  def sort(freqMap: mutable.Map[String, Int], func: printAllType): Unit = func(freqMap.toSeq.sortBy(-_._2), noop)

  type frequenciesType = (Array[String], sortType) => Unit
  def frequencies(words: Array[String], func: sortType): Unit = {
    val freqMap: mutable.Map[String, Int] = mutable.HashMap[String, Int]()
    for(word <- words) freqMap.addOne((word, freqMap.getOrElse(word, 0) + 1))
    func(freqMap, printall)
  }

  type removeStopWordsType = (Array[String], frequenciesType) => Unit
  def removeStopWords(words: Array[String], func: frequenciesType): Unit = {
    val stopWords: mutable.HashSet[String] = mutable.HashSet(('a' to 'z').map(_.toString): _*)
    val stopWordsSource = Source.fromFile("../stop_words.txt")
    stopWords.addAll(stopWordsSource.getLines().next().split(","))
    stopWordsSource.close()
    func(words.filter(!stopWords.contains(_)), sort)
  }

  type scanType = (String, removeStopWordsType) => Unit
  def scan(data: String, func: removeStopWordsType): Unit = func(data.split("\\s+"), frequencies)

  type normalizeType = (String, scanType) => Unit
  def normalize(data: String, func: scanType): Unit = func(data.toLowerCase, removeStopWords)

  type filterCharsType = (String, normalizeType) => Unit
  def filterChars(data: String, func: normalizeType): Unit = func(data.replaceAll("[\\W_]+", " "), scan)

  def readFile(pathToFile: String, func: filterCharsType): Unit = {
    val source = Source.fromFile(pathToFile)
    val data = source.getLines().mkString(" ")
    source.close()
    func(data, normalize)
  }

  def main(args: Array[String]): Unit = {
    readFile(args(0), filterChars)
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