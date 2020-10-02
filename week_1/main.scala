package week_1

import scala.io.Source

object Main {
  def main(args: Array[String]): Unit = {
    val debug = false
    val textFile = if(args.nonEmpty && !debug) args(0) else "../pride-and-prejudice.txt"
    val textSource = Source.fromFile(textFile)
    val text = textSource.getLines().toSeq.
      mkString(" ").
      toLowerCase.
      replaceAll("'s", "").
      replaceAll("[^a-z]", " ")
    val stopWordsFile = "../stop_words.txt"
    val stopWordsSource = Source.fromFile(stopWordsFile)
    val stopWords = stopWordsSource.getLines().next().
      split(",").toSet
    val freqMap = collection.mutable.HashMap[String, Int]()
    for(word <- text.split("\\s+")) {
      if(!stopWords.contains(word)) {
        if(!freqMap.contains(word)) freqMap(word) = 1
        else freqMap(word) += 1
      }
    }
    val mostFrequent = freqMap.toSeq.sortBy(-_._2).slice(0, 25)
    mostFrequent.foreach(p => println(s"${p._1} - ${p._2}"))
    textSource.close()
    stopWordsSource.close()
  }
}