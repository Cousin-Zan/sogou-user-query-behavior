package com.spark.test

import java.sql.{Connection, DriverManager}
import java.util

/**
 * 从mysql连接池中获取连接
 */
class MySqlPool(url: String, user: String, pwd: String) extends Serializable {
  //连接池连接总数
  private val max = 100
  //每次产生连接数
  private val connectionNum = 1
  //当前连接池已产生的连接数
  private var conNum = 0
  //连接池
  private val pool = new util.LinkedList[Connection]()

  //获取连接
  def getJdbcConn(): Connection = {
    //同步代码块，AnyRef为所有引用类型的基类，AnyVal为所有值类型的基类
    AnyRef.synchronized({
      if (pool.isEmpty) {
        //加载驱动
        preGetConn()
        for (i <- 1 to connectionNum) {
          val conn = DriverManager.getConnection(url, user, pwd)
          pool.push(conn)
          conNum += 1
        }
      }
      pool.poll()
    })
  }

  //释放连接
  def releaseConn(conn: Connection): Unit = {
    pool.push(conn)
  }

  //加载驱动
  private def preGetConn(): Unit = {
    //控制加载
    if (conNum > max && pool.isEmpty) {
      //当前连接数大于等于池子最大容纳数线程等待并重新加载
      print("busyness")
      Thread.sleep(2000)
      preGetConn()
    } else {
      Class.forName("com.mysql.jdbc.Driver")
      //当前连接数小于池子最大总数可以加载
    }
  }

}
