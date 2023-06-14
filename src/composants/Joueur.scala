package composants

import ch.hevs.gdx2d.components.physics.primitives.PhysicsCircle
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import ch.hevs.gdx2d.lib.interfaces.DrawableObject
import exp.{Pellet, PelletFactory}

import scala.collection.mutable.ArrayBuffer

class Joueur(val bouboules: PelletFactory, ray: Float, val inPosition: Vector2, val angle: Float) extends DrawableObject {
    val playerBox = new PhysicsCircle("playerCenter", inPosition, ray, angle)
    playerBox.setCollisionGroup(-1)
    private val stats: statSheet = new statSheet(1, 1, 1, 1, 8, 1)
    var Boulettes: ArrayBuffer[Bullet] = new ArrayBuffer[Bullet]()
    var moveRight = false
    var moveLeft = false
    var moveUp = false
    var moveDown = false
    var shooting = false
    var shootingTemp = 0L
    var mouseAngle: Float = 0
    var scalar: Int = 50
    var exp: Int = 0

    def getPos: Vector2 = playerBox.getBodyPosition

    override def draw(g: GdxGraphics): Unit = {
        val pos = playerBox.getBodyPosition
        g.drawFilledCircle(pos.x, pos.y, 30, Color.FIREBRICK)
    }

    /** Fonction de gestion des déplacements du joueur et des tirs*/
    def update(deltaTime: Float): Unit = {

        // Création de la direction
        var baseVector = new Vector2(0, 0)
        // Création et application d'un vecteur de force
        var forceVector = baseVector.scl(scalar)
        val position = playerBox.getBodyWorldCenter

        /** déplacement vertical haut */
        if (moveUp && !moveDown && !moveLeft && !moveRight) {
            baseVector.y += 1
            forceVector = baseVector.scl(scalar)
            //playerBox.setBodyLinearVelocity(forceVector)
            playerBox.applyBodyForce(forceVector, position, true)
        }
        /** déplacement vertical bas */
        if (moveDown && !moveUp && !moveLeft && !moveRight) {
            baseVector.y -= 1
            forceVector = baseVector.scl(scalar)
            //playerBox.setBodyLinearVelocity(forceVector)
            playerBox.applyBodyForce(forceVector, position, true)
        }
        /** déplacement horizontal gauche */
        if (moveLeft && !moveDown && !moveUp && !moveRight) {
            baseVector.x -= 1
            forceVector = baseVector.scl(scalar)
            //playerBox.setBodyLinearVelocity(forceVector)
            playerBox.applyBodyForce(forceVector, position, true)
        }
        /** déplacement horizontal droite */
        if (moveRight && !moveDown && !moveUp && !moveLeft) {
            baseVector.x += 1
            forceVector = baseVector.scl(scalar)
            //playerBox.setBodyLinearVelocity(forceVector)
            layerBox.applyBodyForce(forceVector, position, true)
        }
        if (moveRight && moveDown && !moveUp && !moveLeft) {
            baseVector.x += 1
            baseVector.y -= 1
            forceVector = baseVector.scl(scalar)
            //playerBox.setBodyLinearVelocity(forceVector)
            playerBox.applyBodyForce(forceVector, position, true)
        }
        if (moveRight && !moveDown && moveUp && !moveLeft) {
            baseVector.x += 1
            baseVector.y += 1
            forceVector = baseVector.scl(scalar)
            //playerBox.setBodyLinearVelocity(forceVector)
            playerBox.applyBodyForce(forceVector, position, true)
        }
        if (!moveRight && !moveDown && moveUp && moveLeft) {
            baseVector.x -= 1
            baseVector.y += 1
            forceVector = baseVector.scl(scalar)
            //playerBox.setBodyLinearVelocity(forceVector)
            playerBox.applyBodyForce(forceVector, position, true)
        }
        if (!moveRight && moveDown && !moveUp && moveLeft) {
            baseVector.x -= 1
            baseVector.y -= 1
            forceVector = baseVector.scl(scalar)
            //playerBox.setBodyLinearVelocity(forceVector)
            playerBox.applyBodyForce(forceVector, position, true)
        }

        // Le joueur ralentit si aucune touche directionnelle n'est pressée
        if (!moveUp && !moveDown && !moveLeft && !moveRight) {
            baseVector = new Vector2(0, 0)
            //playerBox.setBodyLinearVelocity(baseVector)
            playerBox.applyBodyForce(forceVector, position, true)
        }


        // Limitation de la vitesse maximum
        val longActu: Float = playerBox.getBodyLinearVelocity.len()
        val vitesseLimite: Int = 10 * stats.movementSpeed
        if (longActu > vitesseLimite) {
            playerBox.setBodyLinearVelocity(playerBox.getBodyLinearVelocity.scl(vitesseLimite / longActu))
        }
        // Gestion du mouvement de recul suite à un tir
        if (shooting && playerBox.getBodyLinearVelocity().isZero) {
            println("Je tire CHEF !")
        }else if(shooting) {
            println("bouge !")
            //playerBox.applyBodyForce(playerBox.getBodyLinearVelocity().scl(0.5f), playerBox.getBodyWorldCenter, true)
        }
        // Gestion des tirs
        if(shooting && shootingTemp < System.currentTimeMillis() - 1000/stats.reload){
            shootingTemp = System.currentTimeMillis()
            val spawnPos: Vector2 = new Vector2(playerBox.getBodyPosition)
            spawnPos.x += 40 * math.sin(mouseAngle * math.Pi / 180).toFloat
            if(mouseAngle < 180) {
                spawnPos.y -= 50 * math.cos(mouseAngle * math.Pi / 180).toFloat
            }else{
                spawnPos.y += 50 * math.cos(mouseAngle * math.Pi / 180).toFloat
            }
            Boulettes.addOne(new Bullet(this.Boulettes,this,10, 10, mouseAngle, spawnPos))
            val backVector = baseVector.scl(-5)
            val positionV = playerBox.getBodyWorldCenter
            playerBox.applyBodyForce(backVector, positionV, true)

        }
    }
    /** Fonction qui aligne le joueur en direction du curseur de la souris */
    def getAngle(v1: Vector2, v2: Vector2): Double = {
        val vInt1: Vector2 = if (v1.x > v2.x) {
            new Vector2(v2.x - v1.x, v2.y - v1.y)
        } else {
            new Vector2(v1.x - v2.x, v1.y - v2.y)
        }
        val vInt2: Vector2 = new Vector2(0, v1.y)
        var dessus: Double = vInt1.y.toDouble * vInt2.y.toDouble
        val dessous: Double = math.sqrt(math.pow(vInt1.x, 2) + math.pow(vInt1.y, 2)) * math.sqrt(math.pow(vInt2.x, 2) + math.pow(vInt2.y, 2))
        if (v1.y < 0) {
            dessus = dessus * -1
        } //Réparation au cas ou la souris est en dessous de la physicBox
        if (v1.x > v2.x) {
            math.acos(dessus / dessous) * 180.0 / math.Pi
        } else {
            360 - math.acos(dessus / dessous) * 180.0 / math.Pi
        }
    }
}