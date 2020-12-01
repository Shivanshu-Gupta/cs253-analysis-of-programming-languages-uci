//package Week4

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import scala.util.matching.Regex

object Fifteen {
  case class WordFrequencyFramework(loadEventHandlers: ArrayBuffer[String => Unit] = ArrayBuffer[String => Unit](),
                                    doworkEventHandlers: ArrayBuffer[() => Unit] = ArrayBuffer[() => Unit](),
                                    endEventHandlers: ArrayBuffer[() => Unit] = ArrayBuffer[() => Unit]()) {
    def registerForLoadEvent(handler: String => Unit): Unit = {
      loadEventHandlers.append(handler)
    }
    def registerForDoworkEvent(handler: () => Unit): Unit = {
      doworkEventHandlers.append(handler)
    }
    def registerForEndEvent(handler: () => Unit): Unit = {
      endEventHandlers.append(handler)
    }
    def run(pathToFile: String): Unit = {
      for(h <- this.loadEventHandlers) h(pathToFile)
      for(h <- this.doworkEventHandlers) h()
      for(h <- this.endEventHandlers) h()
    }
  }

  case class DataStorage(var data: Iterator[String] = Iterator[String](),
                         var stopWordFilter: StopWordFilter,
                         wordEventHandlers: ArrayBuffer[String => Unit] = ArrayBuffer[String => Unit]()) {
    def this(wfapp: WordFrequencyFramework, stopWordFilter: StopWordFilter) = {
      this(stopWordFilter=stopWordFilter)
      wfapp.registerForLoadEvent(load)
      wfapp.registerForDoworkEvent(produceWords)
    }
    def load(pathToFile: String): Unit = {
      data = Source.fromFile(pathToFile).getLines().map(_.toLowerCase)
    }

    def produceWords(): Unit = {
      val wordRegex: Regex = "[a-z]{2,}".r
      for(w <- this.data.flatMap(wordRegex.findAllIn)) {
        if(!this.stopWordFilter.isStopWord(w)) {
          for(h <- wordEventHandlers) h(w)
        }
      }
    }
    def registerForWordEvent(handler: String => Unit): Unit = {
      wordEventHandlers.append(handler)
    }
  }

  case class StopWordFilter(var stopWords: Set[String]) {
    def this(wfapp: WordFrequencyFramework) = {
      this(Set[String]())
      wfapp.registerForLoadEvent(load)
    }
    def load(ignore: String): Unit = {
      val stopWordsSource = Source.fromFile("../stop_words.txt")
      stopWords = stopWordsSource.getLines().next().split(",").toSet
      stopWordsSource.close()
    }
    def isStopWord(word: String) = stopWords.contains(word)
  }

  case class WordFrequencyCounter(wordFreqs: mutable.Map[String, Int]) {
    def this(wfapp: WordFrequencyFramework, dataStorage: DataStorage) = {
      this(mutable.Map[String, Int]())
      dataStorage.registerForWordEvent(incrementCount)
      wfapp.registerForEndEvent(printFreqs)
    }
    def incrementCount(word: String): Unit = {
      wordFreqs += {word -> (wordFreqs.getOrElse(word, 0) + 1)}
    }
    def printFreqs(): Unit = {
      wordFreqs.toSeq.sortBy(-_._2).slice(0, 25).
        foreach(p => println(s"${p._1} - ${p._2}"))
    }
  }

  case class WordsWithZ(wordSet: mutable.Set[String]) {
    def this(wfapp: WordFrequencyFramework, dataStorage: DataStorage) = {
      this(mutable.Set[String]())
      dataStorage.registerForWordEvent(addWord)
      wfapp.registerForEndEvent(printCountWithZ)
    }
    def addWord(word: String): Unit = {
      wordSet += word
    }
    def printCountWithZ(): Unit = {
      println(wordSet.count(_.contains("z")))
    }
  }

  def main(args: Array[String]): Unit = {
    val wfapp = WordFrequencyFramework()
    val stopWordFilter = new StopWordFilter(wfapp)
    val dataStorage = new DataStorage(wfapp=wfapp, stopWordFilter=stopWordFilter)
    new WordFrequencyCounter(wfapp, dataStorage)
    new WordsWithZ(wfapp, dataStorage)
    wfapp.run(args(0))
  }
}
