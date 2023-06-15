package setup

object settings {
  /** valeurs du nombres maximum de chaque pellets */
  val NBR_TRIANGLES = 3
  val NBR_SQUARES = 0
  val NBR_PENTAGONS = 0
  val NBR_BIGPENTAS = 0
  /** Données de la map */
  val INTERNAL_RADIUS: Int = 150
  var EXTERNAL_RADIUS: Int = 750
  val BOX_WIDTH: Int = 12000
  val BOX_HEIGHT: Int = 6000
  val CENTER_X: Int = BOX_WIDTH / 2
  val CENTER_Y: Int = BOX_HEIGHT / 2

  val LEVEL_UPS: Array[Int] = Array(4, 13, 28, 50, 78, 113, 157, 211, 275, 350, 437, 538, 655, 787)
  val NIV_MAX: Int = LEVEL_UPS.length + 1

}
