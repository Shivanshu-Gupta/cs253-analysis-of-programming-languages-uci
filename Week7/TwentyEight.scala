//package Week7

import scala.io.Source
import scala.util.control.Breaks

object TwentyEight {

  def lines(filename: String): Iterator[String] = Source.fromFile(filename).getLines()

  def allWords(filename: String): Iterator[String] = lines(filename).map(_.toLowerCase).flatMap("[a-z]{2,}".r.findAllIn)

  def nonStopWords(filename: String): Iterator[String] = {
    val stopWords: Set[String] = Source.fromFile("../stop_words.txt").getLines().next().split(",").toSet
    allWords(filename).filter(!stopWords.contains(_))
  }

  def countAndSort(filename: String): Iterator[Seq[(String, Int)]] = {
    val it = new Iterator[Seq[(String, Int)]] {
      var wordFreqs: Map[String, Int] = Map[String, Int]()
      val nonStopWordsIter = nonStopWords(filename)
      var i = 1

      override def hasNext: Boolean = nonStopWordsIter.hasNext

      override def next(): Seq[(String, Int)] = {
        val k = 5000
        while(i <= k && nonStopWordsIter.hasNext) {
          val w = nonStopWordsIter.next()
          wordFreqs ++= Map(w -> (wordFreqs.getOrElse(w, 0) + 1))
          i += 1
        }
        i = 1
        wordFreqs.toSeq.sortBy(-_._2)
      }
    }

    it
  }

  def main(args: Array[String]): Unit = {
    val countAndSortIter = countAndSort(args(0))
    for(wordFreqs <- countAndSortIter) {
      println("-----------------------------")
      wordFreqs.take(25).foreach(p => println(s"${p._1} - ${p._2}"))
    }
  }
}
