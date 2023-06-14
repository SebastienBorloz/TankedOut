package exp

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon
import com.badlogic.gdx.math.Vector2

class pentagonPellet(positionIn: Vector2, valIn: Int = 25, healthIn: Int = 60, bodyDamageIn: Int = 25) extends Pellet(valIn, healthIn, bodyDamageIn, positionIn){
    val vectTria: Array[Vector2] = Array(new Vector2(0, 0), new Vector2(0, 50), new Vector2(42.5f, 65), new Vector2(77, 25), new Vector2(42.5f, -15))
    val pentagonBox = new PhysicsPolygon("pentagonPellet", position, vectTria, 60, 0.1f, 1, true)
    pentagonBox.getBody.setGravityScale(healthIn)
}
