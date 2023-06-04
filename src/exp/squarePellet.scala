package exp

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon
import com.badlogic.gdx.math.Vector2

class squarePellet(positionIn: Vector2, valIn: Int = 5, healthIn: Int = 5, bodyDamageIn: Int = 5) extends Pellet(valIn, healthIn, bodyDamageIn, positionIn){
    val vectTria: Array[Vector2] = Array(new Vector2(0, 0), new Vector2(0, 50), new Vector2(50, 50), new Vector2(50, 0))
    val squareBox = new PhysicsPolygon("squarePellet", position, vectTria, 30, 1, 30, true)
}
