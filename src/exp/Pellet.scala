package exp

import com.badlogic.gdx.math.Vector2

trait Pellet {
  val valeur: Int
  val health: Int
  val bodyDamage: Int
  val position: Vector2
}
