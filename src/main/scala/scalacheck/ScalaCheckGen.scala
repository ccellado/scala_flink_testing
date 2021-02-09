package scalacheck

import org.scalacheck.Gen
import org.scalacheck.Properties

import model._

trait ScalaCheckGen {
    val strGen = (n: Int) => Gen.listOfN(n, Gen.alphaChar).map(_.mkString)
    val strNumGen = (n: Int) => Gen.listOfN(n, Gen.numChar).map(_.mkString)
    val testSource: Seq[String] = Gen.containerOfN[Seq, String](100, strGen(20)).sample.get

    def genDevice(usersAndMac: List[(String, Int)]) = (for {
        unit_name <- strGen(5)
        (mac, users) <- usersAndMac
    } yield Devices(unit_name, mac, users))

    def genConns(macAndConns: List[(String, Int)]) = { 
      val map = (for {
        (mac, users) <- macAndConns
      } yield (
          mac
        , (for { devices <- Gen.containerOfN[Seq, String](users, strNumGen(10)) 
          } yield devices).sample.get)
        ).toMap
      Connections(map)
    }
    
    val tuplegen: Gen[(String, Int)] = for {
      name <- strGen(10)
      num <- Gen.choose(0, 42)
    } yield (name, num)

    val testCaseGenerator = for {
      name <- strGen(10)
      macs_num <- Gen.choose(5, 10)
      macs_list <- Gen.containerOfN[List, (String, Int)](macs_num, tuplegen)
    } yield (genConns(macs_list), Unit(name, genDevice(macs_list).sample.get))
}

object ScalaCheckGen extends Properties("connection generates") {
    val res = testCaseGenerator.sample.get
    val connections = (res._1.users_by_mac.map(_._1) zip res._1.users_by_mac.map(_._2.size)).toSet
    val devices = (res._2.device_list.map(_.mac_name) zip res._2.device_list.map(_.users)).toSet
    println(devices diff connections)
}