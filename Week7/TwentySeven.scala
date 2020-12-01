//package Week7

import scala.io.Source

object TwentySeven {

  case class Column[T](var values: Seq[T], func: ()=>Seq[T])

  def main(args: Array[String]): Unit = {
    val allWords = Column[String](null, null)
    val stopWords = Column[String](null, null)
    val nonStopWords = Column[String](null, () =>
      allWords.values.filter(!stopWords.values.contains(_)))
    val uniqueWords = Column[String](null, () =>
      nonStopWords.values.distinct)
    val counts = Column[Int](null, () =>
      uniqueWords.values.map(w => nonStopWords.values.count(_ == w)))
    val sortedData = Column[(String, Int)](null, () =>
      (uniqueWords.values zip counts.values).sortBy(-_._2))
    val allColumns = Seq(nonStopWords, uniqueWords, counts, sortedData)
    def update(): Unit = {
      for(col <- allColumns.filter(_.values == null)) {
        col match {
          case c: Column[String] => c.values = c.func()
          case c: Column[Int] => c.values = c.func()
          case c: Column[(String, Int)] => c.values = c.func()
        }
      }
    }
    stopWords.values = Source.fromFile("../stop_words.txt").getLines().next().split(",").toSeq
    allWords.values = Source.fromFile(args(0)).getLines().map(_.toLowerCase).
      flatMap("[a-z]{2,}".r.findAllIn).toSeq
    update()
    sortedData.values.take(25).foreach(p => println(s"${p._1} - ${p._2}"))
  }
}
