package flink

import org.apache.flink.streaming.api.functions.sink.SinkFunction

class CollectSink[T] extends SinkFunction[T] {
  override def invoke(value: T) = {
    CollectSink.values = value :: CollectSink.values
  }
}

object CollectSink { 
  var values: List[_] = List()
}