package exp

import ch.hevs.gdx2d.components.physics.primitives.PhysicsPolygon
import com.badlogic.gdx.math.Vector2

class bigPentaPellet(pos: Vector2, value: Int = 100, vie: Int = 1000, bdyDamage: Int = 100)
  extends PhysicsPolygon("bigPentaPellet", pos, Array(new Vector2(0, 0), new Vector2(0, 100), new Vector2(95, 130), new Vector2(154, 50), new Vector2(95, -30)), 120, 0.1f, 1, true)
    with Pellet {

    override val position: Vector2 = pos
    override val health: Int = vie
    override val valeur: Int = value
    override val bodyDamage: Int = bdyDamage
}
