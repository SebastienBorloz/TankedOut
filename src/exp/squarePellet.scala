package exp

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon
import com.badlogic.gdx.math.Vector2

class squarePellet(pos: Vector2, value: Int = 5, vie: Int = 60, bdyDamage: Int = 5)
  extends PhysicsPolygon("squarePellet", pos, Array(new Vector2(0, 0), new Vector2(0, 50), new Vector2(50, 50), new Vector2(50, 0)), 30, 0.1f, 1, true)
    with Pellet{
    override val position: Vector2 = pos
    override val health: Int = vie
    override val valeur: Int = value
    override val bodyDamage: Int = bdyDamage
}
