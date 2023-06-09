package composants

import ch.hevs.gdx2d.components.physics.primitives.PhysicsBox
import ch.hevs.gdx2d.components.physics.utils.PhysicsConstants
import ch.hevs.gdx2d.lib.physics.PhysicsWorld
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef


class Canon(var joueur: Joueur, val canonPos: Vector2, val width: Float, val length: Float) {
    val world: World = PhysicsWorld.getInstance
    val x: Vector2 = PhysicsConstants.coordPixelsToMeters(canonPos)
    var body: Body = null
    // Convert player position to pixels
    val pos: Vector2 = joueur.getPos
    pos.x += 30

    // Create the canon
    val canon = new PhysicsBox("wheel", pos, width, length / 2, joueur.playerBox.getBodyAngle)
    this.body = canon.getBody


    var jointDef: RevoluteJointDef = new RevoluteJointDef()
    jointDef.initialize(joueur.playerBox.getBody(), this.body, this.body.getWorldCenter())
    jointDef.enableMotor = true; //we'll be controlling the wheel's angle manually
    world.createJoint(jointDef)

}