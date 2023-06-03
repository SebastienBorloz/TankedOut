package exp

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon
import com.badlogic.gdx.math.Vector2



class trianglePellet(positionIn: Vector2, valIn: Int = 1, healthIn: Int = 1, bodyDamageIn: Int = 1) extends Pellet(valIn, healthIn, bodyDamageIn, positionIn){
    val vectTria: Array[Vector2] = Array(new Vector2(0, 0), new Vector2(0, 50), new Vector2(43.3f, 25))
    val triangleBox = new PhysicsPolygon("playerCenter", position, vectTria,1,1,1,true)
}
