package composants

import ch.hevs.gdx2d.components.physics.primitives.PhysicsBox
import ch.hevs.gdx2d.components.physics.utils.PhysicsConstants
import ch.hevs.gdx2d.lib.physics.PhysicsWorld
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef


class Canon(var joueur: Joueur, val canonPos: Vector2, val width: Float, val length: Float, var shooting: Boolean) {
  val world: World = PhysicsWorld.getInstance
  val x: Vector2 = PhysicsConstants.coordPixelsToMeters(canonPos)
  // Convert player position to pixels
  val pos: Vector2 = joueur.playerBox.getBody.getWorldPoint(x)
  // Create the wheel
  val wheel = new PhysicsBox("wheel", PhysicsConstants.coordMetersToPixels(pos), width, length / 2, joueur.playerBox.getBodyAngle)
  this.body = wheel.getBody
  var body: Body = null
}
