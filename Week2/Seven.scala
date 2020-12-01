//package Week2

import scala.io.Source
import scala.util.matching.Regex

object Seven {
  def main(args: Array[String]): Unit = {
    val text: String = Source.fromFile(args(0)).getLines().mkString(" ").toLowerCase
    val stopWords: Set[String] = Source.fromFile("../stop_words.txt").getLines().next().split(",").toSet
    val wordRegex: Regex = "[a-z]{2,}".r
    val freqMap: Map[String, Int] = wordRegex.findAllIn(text).toSeq.filter(!stopWords.contains(_)).groupBy(identity).transform((_, v) => v.length)
    freqMap.toSeq.sortBy(-_._2).slice(0, 25).foreach(p =>
      println(s"${p._1} - ${p._2}"))
  }
}
