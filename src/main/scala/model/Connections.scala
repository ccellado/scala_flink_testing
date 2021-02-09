package model

case class Connections(users_by_mac: Map[String, Seq[String]])
case class Devices(unit_name: String, mac_name: String, users: Int)
case class Unit(name: String, device_list: Seq[Devices])