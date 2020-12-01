//package Week4

import scala.collection.mutable
import scala.io.Source
import scala.util.matching.Regex

object Thirteen {
  def extractWords(obj: mutable.Map[String, Any], pathToFile: String): Unit = {
    val wordRegex: Regex = "[a-z]{2,}".r
    obj("data") = Source.fromFile(pathToFile).getLines().
      map(_.toLowerCase).
      flatMap(wordRegex.findAllIn).toSeq
  }

  def loadStopWords(obj: mutable.Map[String, Any]): Unit = {
    val stopWordsSource = Source.fromFile("../stop_words.txt")
    obj("stop_words") = stopWordsSource.getLines().next().split(",").toSet
    stopWordsSource.close()
  }

  def incrementCount(obj: mutable.Map[String, Any], w: String): Unit = {
    if(!obj("freqs").asInstanceOf[mutable.Map[String, Int]].contains(w)) {
      obj("freqs").asInstanceOf[mutable.Map[String, Int]](w) = 0
    }
    obj("freqs").asInstanceOf[mutable.Map[String, Int]](w) += 1
  }

  def main(args: Array[String]): Unit = {

    lazy val dataStorageObj: mutable.Map[String, Any] = mutable.Map(
      "data" -> Seq[String](),
      "init" -> {(pathToFile: String) => extractWords(dataStorageObj, pathToFile)},
      "words" -> {() => dataStorageObj("data")}
    )

    lazy val stopWordsObj: mutable.Map[String, Any] = mutable.Map(
      "stop_words" -> Set[String](),
      "init" -> {() => loadStopWords(stopWordsObj)},
      "is_stop_word" -> {(word: String) => stopWordsObj("stop_words").
        asInstanceOf[Set[String]].contains(word)}
    )

    lazy val wordFreqsObj: mutable.Map[String, Any] = mutable.Map(
      "freqs" -> mutable.Map[String, Int](),
      "increment_count" -> {(word: String) => incrementCount(wordFreqsObj, word)},
      "sorted" -> {() => wordFreqsObj("freqs").asInstanceOf[mutable.Map[String, Int]].toSeq.sortBy(-_._2)}
    )

    dataStorageObj("init").asInstanceOf[String => Unit](args(0))
    stopWordsObj("init").asInstanceOf[()=>Unit]()

    for(w: String <- dataStorageObj("words").asInstanceOf[()=>Seq[String]]()) {
      if(!stopWordsObj("is_stop_word").asInstanceOf[String => Boolean](w)) {
        wordFreqsObj("increment_count").asInstanceOf[String => Unit](w)
      }
    }

    wordFreqsObj("top25") = {() => wordFreqsObj("sorted").asInstanceOf[()=> Seq[(String, Int)]]().
      take(25).foreach(p => println(s"${p._1} - ${p._2}"))}
    wordFreqsObj("top25").asInstanceOf[() => Unit]()
  }
}
