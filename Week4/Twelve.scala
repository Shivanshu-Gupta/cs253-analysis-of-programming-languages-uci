//package Week4

import scala.io.Source
import scala.util.matching.Regex

object Twelve {
  case class DataStorageManager(var lines: Iterator[String] = Iterator[String]()) {
    def dispatch(message: Seq[String]): Any = {
      if(message.head == "init")
        init(message(1))
      else if(message.head == "words")
        words()
      else {
        throw new Exception("Message not understood " + message.head)
      }
    }

    def init(pathToFile: String): Unit = {
      lines = Source.fromFile(pathToFile).getLines().map(_.toLowerCase)
    }

    def words(): Iterator[String] = {
      val wordRegex: Regex = "[a-z]{2,}".r
      lines.flatMap(wordRegex.findAllIn)
    }
  }

  case class StopWordManager(var stopWords: Set[String] = Set[String]()) {
    def dispatch(message: Seq[String]): Any = {
      if(message.head == "init") init()
      else if(message.head == "is_stop_word") isStopWord(message(1))
      else throw new Exception("Message not understood " + message.head)
    }

    def init(): Unit = {
      val stopWordsSource = Source.fromFile("../stop_words.txt")
      stopWords = stopWordsSource.getLines().next().split(",").toSet
      stopWordsSource.close()
    }

    def isStopWord(word: String): Boolean = stopWords.contains(word)
  }

  case class WordFrequencyManager(var wordFreqs: Map[String, Int] = Map[String, Int]()) {
    def dispatch(message: Seq[String]): Any = {
      if(message.head == "increment_count") incrementCount(message(1))
      else if(message.head == "sorted") sorted()
      else throw new Exception("Message not understood " + message.head)
    }

    def incrementCount(word: String): Unit = {
      wordFreqs ++= Map(word -> (wordFreqs.getOrElse(word, 0) + 1))
    }

    def sorted(): Seq[(String, Int)] = wordFreqs.toSeq.sortBy(-_._2)
  }

  case class WordFrequencyController(storageManager: DataStorageManager = DataStorageManager(),
                                stopWordManager: StopWordManager = StopWordManager(),
                                wordFrequencyManager: WordFrequencyManager = WordFrequencyManager()) {
    def dispatch(message: Seq[String]): Any = {
      if(message.head == "init") init(message(1))
      else if(message.head == "run") run()
      else throw new Exception("Message not understood " + message.head)
    }

    def init(pathToFile: String): Unit = {
      storageManager.dispatch(Seq("init", pathToFile))
      stopWordManager.dispatch(Seq("init"))
    }

    def run(): Unit = {
      val words: Iterator[String] = storageManager.dispatch(Seq("words")).asInstanceOf[Iterator[String]]
      for(w <- words) {
        if(!stopWordManager.dispatch(Seq("is_stop_word", w)).asInstanceOf[Boolean]) {
          wordFrequencyManager.dispatch(Seq("increment_count", w))
        }
      }
      wordFrequencyManager.dispatch(Seq("sorted")).asInstanceOf[Seq[(String, Int)]].
        take(25).foreach(p => println(s"${p._1} - ${p._2}"))
    }
  }

  def main(args: Array[String]): Unit = {
    val wfController = WordFrequencyController()
    wfController.dispatch(Seq("init", args(0)))
    wfController.dispatch(Seq("run"))
  }
}
