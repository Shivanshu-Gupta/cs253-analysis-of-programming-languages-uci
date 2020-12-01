//package Week6

import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import scala.collection.mutable
import scala.io.Source
import scala.util.matching.Regex

object TwentyNine {
  abstract class ActiveWFObject(val queue: BlockingQueue[Seq[Any]] = new LinkedBlockingQueue[Seq[Any]],
                                var stopMe: Boolean = false) extends Thread {
    start()
    override def run(): Unit = {
      while(!stopMe) {
        val message = queue.take()
        dispatch(message)
        if(message.head == "die") stopMe = true
      }
    }
    def dispatch(message: Seq[Any]): Unit
  }

  def send(receiver: ActiveWFObject, message: Seq[Any]): Unit = {
    receiver.queue.put(message)
  }

  case class DataStorageManager(var lines: Iterator[String] = Iterator[String](),
                                var stopWordManager: StopWordManager = null) extends ActiveWFObject {
    def dispatch(message: Seq[Any]): Unit = {
      if(message.head.asInstanceOf[String] == "init") init(message.tail)
      else if(message.head.asInstanceOf[String] == "send_word_freqs") processWords(message.tail)
      else send(stopWordManager, message)
    }

    def init(message: Seq[Any]): Unit = {
      val pathToFile = message.head.asInstanceOf[String]
      stopWordManager = message(1).asInstanceOf[StopWordManager]
      lines = Source.fromFile(pathToFile).getLines().map(_.toLowerCase)
    }

    def processWords(message: Seq[Any]): Unit = {
      val recipient = message.head.asInstanceOf[ActiveWFObject]
      val wordRegex: Regex = "[a-z]{2,}".r
      for(w <- lines.flatMap(wordRegex.findAllIn).toSeq) {
        send(stopWordManager, Seq("filter", w))
      }
      send(stopWordManager, Seq("top25", recipient))
    }
  }

  case class StopWordManager(var stopWords: Set[String] = Set[String](),
                             var wordFrequencyManager: WordFrequencyManager = null) extends ActiveWFObject {
    def dispatch(message: Seq[Any]): Unit = {
      if(message.head.asInstanceOf[String] == "init") init(message.tail)
      else if(message.head.asInstanceOf[String] == "filter") filter(message.tail)
      else send(wordFrequencyManager, message)
    }

    def init(message: Seq[Any]): Unit = {
      wordFrequencyManager = message.head.asInstanceOf[WordFrequencyManager]
      val stopWordsSource = Source.fromFile("../stop_words.txt")
      stopWords = stopWordsSource.getLines().next().split(",").toSet
      stopWordsSource.close()
    }

    def filter(message: Seq[Any]): Unit = {
      val word = message.head.asInstanceOf[String]
      if(!stopWords.contains(word)) send(wordFrequencyManager, Seq("word", word))
    }
  }

  case class WordFrequencyManager(var wordFreqs: Map[String, Int] = Map[String, Int]()) extends ActiveWFObject {
    def dispatch(message: Seq[Any]): Unit = {
      if(message.head.asInstanceOf[String] == "word") incrementCount(message.tail)
      else if(message.head.asInstanceOf[String] == "top25") top25(message.tail)
    }

    def incrementCount(message: Seq[Any]): Unit = {
      val word = message.head.asInstanceOf[String]
      wordFreqs ++= Map(word -> (wordFreqs.getOrElse(word, 0) + 1))
    }

    def top25(message: Seq[Any]): Unit = {
      val recipient = message.head.asInstanceOf[ActiveWFObject]
      send(recipient, Seq("top25", wordFreqs.toSeq.sortBy(-_._2)))
    }
  }

  case class WordFrequencyController(var storageManager: DataStorageManager = null) extends ActiveWFObject {
    def dispatch(message: Seq[Any]): Unit = {
      if(message.head.asInstanceOf[String] == "run") run(message.tail)
      else if(message.head.asInstanceOf[String] == "top25") display(message.tail)
      else throw new Exception("Message not understood " + message.head.asInstanceOf[String])
    }

    def run(message: Seq[Any]): Unit = {
      storageManager = message.head.asInstanceOf[DataStorageManager]
      send(storageManager, Seq("send_word_freqs", this))
    }

    def display(message: Seq[Any]): Unit = {
      message.head.asInstanceOf[Seq[(String, Int)]].
        take(25).foreach(p => println(s"${p._1} - ${p._2}"))
      send(storageManager, Seq("die"))
      stopMe = true
    }
  }

  def main(args: Array[String]): Unit = {
    val wordFrequencyManager = WordFrequencyManager()

    val stopWordManager = StopWordManager()
    send(stopWordManager, Seq("init", wordFrequencyManager))

    val storageManager = DataStorageManager()
    send(storageManager, Seq("init", args(0), stopWordManager))

    val wfController = WordFrequencyController()
    send(wfController, Seq("run", storageManager))

    for(t <- Seq(wordFrequencyManager, stopWordManager, storageManager, wfController)) t.join()
  }
}
