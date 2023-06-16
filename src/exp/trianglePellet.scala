package exp

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon
import com.badlogic.gdx.math.Vector2

class trianglePellet(pos: Vector2, value: Int = 10, vie: Int = 20, bdyDamage: Int = 1)
  extends PhysicsPolygon("trianglePellet", pos,
                        Array(new Vector2(0, 0), new Vector2(0, 50), new Vector2(43.3f, 25)),
                10,0.1f,1,true)
  with Pellet{

    override val position: Vector2 = pos
    override var health: Int = vie
    override val valeur: Int = value
    override val bodyDamage: Int = bdyDamage
}
