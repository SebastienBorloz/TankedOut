package ch.hevs.gdx2d

import composants.Joueur
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries
import ch.hevs.gdx2d.desktop.physics.DebugRenderer
import ch.hevs.gdx2d.lib.physics.PhysicsWorld
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.desktop.PortableApplication
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import exp.trianglePellet


/**
 * Hello World demo.
 *
 * @author Christopher Métrailler (mei)
 * @author Pierre-André Mudry (mui)
 * @version 2.1
 */
object Main {
    def main(args: Array[String]): Unit = {
        new Main
    }
}

class Main extends PortableApplication(2000, 1000) {
    private var dbgRenderer: DebugRenderer = null
    private var imgBitmap: BitmapImage = null
    private var fong: BitmapImage = null
    private val world = PhysicsWorld.getInstance
    private var zoom = .0
    private var p1: Joueur = null

    override def onInit(): Unit = { // No gravity in this world
        world.setGravity(new Vector2(0, 0))
        setTitle("Test map")

        /*imgBitmap = new BitmapImage("data/images/hei-pi.png")
        fong = new BitmapImage("data/images/sapm.png")*/
        dbgRenderer = new DebugRenderer

        // Create the obstacles in the scene
        new PhysicsScreenBoundaries(getWindowWidth * 3, getWindowHeight * 3)

        // Our car
        //c1 = new Car(30, 70, new Vector2(200, 200), (float) Math.PI, 10, 30, 15);
        p1 = new Joueur(30, new Vector2(200, 200), 0)
        zoom = 2

        val ui: trianglePellet = new trianglePellet(new Vector2(100,100))
    }

    /**
     * Animation related variables
     */
    private val direction = 1
    private val currentTime = 0
    final private val ANIMATION_LENGTH = 2f // Animation length (in seconds)

    final private val MIN_ANGLE = -20
    final private val MAX_ANGLE = 20

    override def onGraphicRender(g: GdxGraphics): Unit = {
        g.clear()
        //g.drawBackground(fong, 1, 1);
        // Physics update
        PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime)
        // Camera follows the hero
        g.zoom(zoom.toFloat)
        g.moveCamera((p1.getPos.x - getWindowWidth / 2 * zoom).toFloat, (p1.getPos.y - getWindowHeight / 2 * zoom).toFloat)

        /**
         * Move the car according to key presses
         */
        if (Gdx.input.isKeyPressed(Input.Keys.W)) if (zoom > 0.2) zoom -= 0.1
        if (Gdx.input.isKeyPressed(Input.Keys.S)) if (zoom < 5) zoom += 0.1

        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) p1.moveUp = true
        else p1.moveUp = false
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) p1.moveDown = true
        else p1.moveDown = false
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) p1.moveLeft = true
        else p1.moveLeft = false
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) p1.moveRight = true
        else p1.moveRight = false

        p1.update(Gdx.graphics.getDeltaTime)
        p1.draw(g)
        dbgRenderer.render(world, g.getCamera.combined)

        g.drawFPS()
        g.drawSchoolLogo()
    }
}
