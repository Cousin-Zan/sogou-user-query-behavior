
import utils.netAnalyzer
import org.apache.spark._
import org.json4s.DefaultFormats
import org.json4s.jackson.Json

import scala.collection.JavaConverters._

/**
 * Created by Administrator on 2016/4/6.
 */
object AnalyzerDemo {

  //分词排序后取出词频最高的前50个
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("my app").setMaster("local")
    val sc = new SparkContext(conf)

    val javaList: java.util.List[String] = netAnalyzer.anaylyzerWords()
    val list = javaList.asScala.toList
    val result = list.map((_, 1)).groupBy(_._1).mapValues(_.size)

    val x=collection.mutable.LinkedHashMap(result.toSeq.sortBy(_._2):_*)
    val x1 = x.filter(x=>{x._2 >= 1000 })

    println(Json(DefaultFormats).write(x1))
  }
}