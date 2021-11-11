package com.spark.test

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.ProcessingTime

/**
 * 结构化流从kafka中读取数据存储到关系型数据库mysql
 * 目前结构化流对kafka的要求版本0.10及以上
 */

object StructuredStreamingKafka {

  System.setProperty("hadoop.home.dir", "D:\\hadoop-common-2.2.0-bin")

  //对象匹配
  case class Weblog(datatime: String, //访问时间
                    userid: String, //用户ID
                    searchname: String, //[查询词]
                    retorder: String, //该URL在返回结果中的排名
                    cliorder: String, //用户点击的顺序号
                    cliurl: String) //用户点击的URL

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .master("local[2]")
      .appName("streaming").getOrCreate()
    //df 流数据输入dataframe
    val df = spark
      .readStream
      .format("kafka") //流数据来源
      .option("kafka.bootstrap.servers", "master:9092") //流数据源主机
      .option("subscribe", "weblogs") //订阅的topic
      .load()

    import spark.implicits._
    val lines = df.selectExpr("CAST(value AS STRING)").as[String]
    val weblog = lines.map(_.split(","))
      .map(x => Weblog(x(0), x(1), x(2), x(3), x(4), x(5)))
    val titleCount = weblog
      .groupBy("searchname").count().toDF("titleName", "count")

    val url = "jdbc:mysql://master:3306/test?useSSL=false"
    val username = "root"
    val password = "123456"

    val writer = new JDBCSink(url, username, password)
    val query = titleCount.writeStream
      .foreach(writer)
      .outputMode("update")
      .trigger(ProcessingTime("6 seconds"))
      .start()
    query.awaitTermination()
  }
}
