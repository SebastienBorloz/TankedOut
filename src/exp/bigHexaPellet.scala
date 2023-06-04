package exp

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon
import com.badlogic.gdx.math.Vector2

class bigHexaPellet(positionIn: Vector2, valIn: Int = 100, healthIn: Int = 100, bodyDamageIn: Int = 100) extends Pellet(valIn, healthIn, bodyDamageIn, positionIn){
  val vectTria: Array[Vector2] = Array(new Vector2(0, 0), new Vector2(0, 100), new Vector2(95, 130), new Vector2(154, 50), new Vector2(95, -30))
  val bigHexaBox = new PhysicsPolygon("biHexaPellet", position, vectTria, 30, 1, 30, true)
}
