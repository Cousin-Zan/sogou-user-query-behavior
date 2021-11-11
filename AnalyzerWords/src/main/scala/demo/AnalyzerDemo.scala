package demo

import org.apache.spark._
import utils.AnaylyzerTools

/**
 * Created by Administrator on 2016/4/6.
 */
object AnalyzerDemo {

  //分词排序后取出词频最高的前50个
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("my app").setMaster("local")
    val sc = new SparkContext(conf)
    val data = sc.textFile("D:\\searchname.txt").map(x => {
      val list = AnaylyzerTools.anaylyzerWords(x) //分词处理
      list.toString.replace("[", "").replace("]", "").split(",")
    }).flatMap(x => x.toList).map(x => (x.trim(), 1)).reduceByKey(_ + _).top(50)(Ordering.by(x => x._2)).foreach(println)
  }


  //分词排序
  object Ord extends Ordering[(String, Int)] {
    def compare(a: (String, Int), b: (String, Int)) = a._2 compare (b._2)
  }

}