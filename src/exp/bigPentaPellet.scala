package exp

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon
import com.badlogic.gdx.math.Vector2

class bigPentaPellet(positionIn: Vector2, valIn: Int = 100, healthIn: Int = 100, bodyDamageIn: Int = 100) extends Pellet(valIn, healthIn, bodyDamageIn, positionIn){
    val vectTria: Array[Vector2] = Array(new Vector2(0, 0), new Vector2(0, 100), new Vector2(95, 130), new Vector2(154, 50), new Vector2(95, -30))
    val bigPentaBox = new PhysicsPolygon("bigPentaPellet", position, vectTria, 120, 0.1f, 1, true)
    bigPentaBox.getBody.setGravityScale(healthIn)
}
