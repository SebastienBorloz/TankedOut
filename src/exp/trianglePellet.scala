package exp

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon
import com.badlogic.gdx.math.Vector2



class trianglePellet(positionIn: Vector2, valIn: Int = 1, healthIn: Int = 20, bodyDamageIn: Int = 1) extends Pellet(valIn, healthIn, bodyDamageIn, positionIn){
    val vectTria: Array[Vector2] = Array(new Vector2(0, 0), new Vector2(0, 50), new Vector2(43.3f, 25))
    val triangleBox = new PhysicsPolygon("trianglePellet", position, vectTria,10,0.1f,1,true)
    triangleBox.getBody.setGravityScale(healthIn)
}
