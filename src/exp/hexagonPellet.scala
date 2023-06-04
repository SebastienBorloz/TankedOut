package exp

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon
import com.badlogic.gdx.math.Vector2

class hexagonPellet(positionIn: Vector2, valIn: Int = 25, healthIn: Int = 25, bodyDamageIn: Int = 25) extends Pellet(valIn, healthIn, bodyDamageIn, positionIn){
  val vectTria: Array[Vector2] = Array(new Vector2(0, 0), new Vector2(0, 25), new Vector2(23.75f, 32.5f), new Vector2(38.5f, 12.5f), new Vector2(23.75f, -7.5f))
  val hexagonBox = new PhysicsPolygon("hexagonPellet", position, vectTria, 30, 1, 30, true)
}
