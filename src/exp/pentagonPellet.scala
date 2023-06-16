package exp

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon
import com.badlogic.gdx.math.Vector2

class pentagonPellet(pos: Vector2, value: Int = 40, vie: Int = 250, bdyDamage: Int = 25)
  extends PhysicsPolygon("pentagonPellet", pos, Array(new Vector2(0, 0), new Vector2(0, 50), new Vector2(42.5f, 65), new Vector2(77, 25), new Vector2(42.5f, -15)), 60, 0.1f, 1, true)
    with Pellet{

    override val position: Vector2 = pos
    override var health: Int = vie
    override val valeur: Int = value
    override val bodyDamage: Int = bdyDamage
}
