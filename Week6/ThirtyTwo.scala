//package Week6

import scala.collection.mutable
import scala.io.Source

object ThirtyTwo {
  def splitWords(lines: Seq[String]): Seq[(String, Int)] = {
    val stopWords = Source.fromFile("../stop_words.txt").getLines().next().split(",").toSet
    val wordRegex = "[a-z]{2,}".r
    lines.map(_.toLowerCase).flatMap(wordRegex.findAllIn).
      filter(!stopWords.contains(_)).
      map((_, 1))
  }

  def regroup(pairsList: Seq[Seq[(String, Int)]]) = {
    val mapping = Seq("a-e", "f-j", "i-o", "p-t", "u-z").map((_, mutable.ArrayBuffer[(String, Int)]())).toMap
    for(pairs <- pairsList) {
      for(p <- pairs) {
        if(('a' to 'e').toSet.contains(p._1.head)) mapping("a-e").append(p)
        else if(('f' to 'j').toSet.contains(p._1.head)) mapping("f-j").append(p)
        else if(('i' to 'o').toSet.contains(p._1.head)) mapping("i-o").append(p)
        else if(('p' to 't').toSet.contains(p._1.head)) mapping("p-t").append(p)
        else mapping("u-z").append(p)
      }
    }
    mapping
  }

  def countWords(mapping: (String, mutable.ArrayBuffer[(String, Int)])): Seq[(String, Int)] = {
    val pairs = mapping._2
    val wordFreqs = mutable.Map[String, Int]()
    for((k, v) <- pairs) {
      wordFreqs ++= Map(k -> (wordFreqs.getOrElse(k, 0) + v))
    }
    wordFreqs.toSeq
  }

  def main(args: Array[String]): Unit = {
    val splits = Source.fromFile(args(0)).getLines().sliding(200, 200).toSeq.map(splitWords)
    val splitsPerCharset = regroup(splits)
    val wordFreqs: Seq[(String, Int)] = splitsPerCharset.toSeq.flatMap(countWords).sortBy(-_._2)
    wordFreqs.take(25).foreach(p => println(s"${p._1} - ${p._2}"))
  }
}
