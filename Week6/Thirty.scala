//package Week6

import java.util.concurrent.{LinkedBlockingQueue, TimeUnit}

import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.control.Breaks

object Thirty {
  val wordSpace = new LinkedBlockingQueue[String]()
  val freqSpace = new LinkedBlockingQueue[Map[String, Int]]()
  val stopWords = Source.fromFile("../stop_words.txt").getLines().next().split(",").toSet

  def processWords(): Unit = {
    var wordFreqs: Map[String, Int] = Map[String, Int]()
    val loop = new Breaks
    loop.breakable {
      while(true) {
        val word = wordSpace.poll(1, TimeUnit.SECONDS)
        if(word == null) loop.break()
        if(!stopWords.contains(word)) {
          wordFreqs ++= Map(word -> (wordFreqs.getOrElse(word, 0) + 1))
        }
      }
    }
    freqSpace.put(wordFreqs)
  }
  def main(args: Array[String]): Unit = {
    val lines = Source.fromFile(args(0)).getLines().map(_.toLowerCase)
    for (word <- lines.flatMap("[a-z]{2,}".r.findAllIn)) {
      wordSpace.put(word)
    }
    val workers = ArrayBuffer[Thread]()
    for(_ <- 0 to 5) {
      workers.append(new Thread{
        processWords()
      })
    }
    for(worker <- workers) worker.start()
    for(worker <- workers) worker.join()

    var wordFreqs: Map[String, Int] = Map[String, Int]()
    while(!freqSpace.isEmpty) {
      val freqs = freqSpace.poll()
      for((k, v) <- freqs) {
        wordFreqs ++= Map(k -> (wordFreqs.getOrElse(k, 0) + v))
      }
    }
    wordFreqs.toSeq.sortBy(-_._2).
      take(25).foreach(p => println(s"${p._1} - ${p._2}"))
  }
}
