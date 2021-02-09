package flink

import org.scalacheck._
import org.scalatest._
import org.scalatestplus.scalacheck._
import org.scalatest.flatspec.AnyFlatSpec
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala.{DataStream, DataStreamUtils, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.scala.createTypeInformation
import org.apache.flink.test.util.MiniClusterWithClientResource
import org.apache.flink.test.util.MiniClusterResourceConfiguration
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.slf4j.{Logger, LoggerFactory}
import scalacheck.ScalaCheckGen

object FlinkJob extends App with ScalaCheckGen {
  override def main(args: Array[String]) = {
    val log: Logger = LoggerFactory.getLogger(this.getClass)
    val flinkCluster = new MiniClusterWithClientResource(new MiniClusterResourceConfiguration.Builder()
      .setNumberSlotsPerTaskManager(1)
      .setNumberTaskManagers(1)
      .build)
    
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(2)
    
    val testStreamSource = env.fromCollection(testSource) 
    def testStream(src: DataStream[String]) = src.map(x => s"TEST + ${x}")
  
    val sink = new CollectSink[String]()
    testStream(testStreamSource).addSink(sink)

    env.execute()

    println(CollectSink.values)
  }
}
