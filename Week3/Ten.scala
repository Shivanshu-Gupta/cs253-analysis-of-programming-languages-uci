//package Week3

import scala.io.Source
import scala.util.matching.Regex

object Ten {
  implicit class SearchableString(s: String) {
    def findAll(regex: Regex): Regex.MatchIterator = regex.findAllIn(s)
  }
  def main(args: Array[String]): Unit = {
    val lines: Iterator[String] = Source.fromFile(args(0)).getLines()
    val stopWords: Set[String] = Source.fromFile("../stop_words.txt").getLines().next().split(",").toSet
    val wordRegex: Regex = "[a-z]{2,}".r
    lines.map(_.toLowerCase).
      flatMap(wordRegex.findAllIn).filter(!stopWords.contains(_)).
      foldLeft(Map[String, Int]())((freqMap, w) => freqMap ++ Map(w -> (freqMap.getOrElse(w, 0) + 1))).
      toSeq.sortBy(-_._2).slice(0, 25).
      foreach(p => println(s"${p._1} - ${p._2}"))
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