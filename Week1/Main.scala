// package Week1

import scala.io.Source

object Main {
  def tf1(rawText: String, stopWords: Set[String], freqMap: collection.mutable.Map[String, Int]): Unit = {
    val text = rawText.replaceAll("[^a-z]", " ")
    for(word <- text.split("\\s+")) {
      if(word.length > 1 && !stopWords.contains(word)) {
        freqMap.addOne((word, freqMap.getOrElse(word, 0) + 1))
      }
    }
  }

  def tf2(rawText: String, stopWords: Set[String], freqMap: collection.mutable.Map[String, Int]): Unit = {
    val words = "[a-z]{2,}".r
    for(word <- words.findAllIn(rawText).filter(!stopWords.contains(_))) {
      freqMap.addOne((word, freqMap.getOrElse(word, 0) + 1))
    }
  }

  def main(args: Array[String]): Unit = {
    val debug = false
    val textFile = if(args.nonEmpty && !debug) args(0) else "../pride-and-prejudice.txt"
    val textSource = Source.fromFile(textFile)
    val text = textSource.getLines().toSeq.
      mkString(" ").
      toLowerCase
    val stopWordsFile = "../stop_words.txt"
    val stopWordsSource = Source.fromFile(stopWordsFile)
    val stopWords = stopWordsSource.getLines().next().split(",").toSet
    val freqMap = collection.mutable.HashMap[String, Int]()
    tf1(text, stopWords, freqMap)
    val mostFrequent = freqMap.toSeq.sortBy(-_._2).slice(0, 25)
    mostFrequent.foreach(p => println(s"${p._1} - ${p._2}"))
    textSource.close()
    stopWordsSource.close()
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