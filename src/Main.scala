package ch.hevs.gdx2d

import ch.hevs.gdx2d.componants.Joueur
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
import ch.hevs.gdx2d.componants.Car


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

class Main extends PortableApplication {
  private[gdx2d] var dbgRenderer = null
  private var imgBitmap = null
  private var fong = null
  private[gdx2d] val world = PhysicsWorld.getInstance
  private var zoom = .0
  //Car c1;
  private[gdx2d] var p1 = null

  override def onInit(): Unit = { // No gravity in this world
    world.setGravity(new Vector2(0, 0))
    setTitle("Test map")
    // Load a custom image (or from the lib "res/lib/icon64.png")
    imgBitmap = new BitmapImage("data/images/hei-pi.png")
    fong = new BitmapImage("data/images/sapm.png")
    dbgRenderer = new DebugRenderer
    // Create the obstacles in the scene
    new PhysicsScreenBoundaries(getWindowWidth * 10, getWindowHeight * 10)
    // Our car
    //c1 = new Car(30, 70, new Vector2(200, 200), (float) Math.PI, 10, 30, 15);
    p1 = new Joueur(30, new Vector2(200, 200), 0)
    zoom = 2
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
    g.zoom(zoom)
    g.moveCamera(p1.getPos.x - getWindowWidth / 2 * zoom, p1.getPos.y - getWindowWidth / 2 * zoom)

    /**
     * Move the car according to key presses
     */
    if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) p1.moveUp = true
    else p1.moveUp = false
    if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) p1.moveDown = true
    else p1.moveDown = false
    if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) p1.moveLeft = true
    else p1.moveLeft = false
    if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) p1.moveRight = true
    else p1.moveRight = false
    if (Gdx.input.isKeyPressed(Input.Keys.W)) if (zoom > 0.2) zoom -= 0.1
    if (Gdx.input.isKeyPressed(Input.Keys.S)) if (zoom < 3) zoom += 0.1
    p1.update(Gdx.graphics.getDeltaTime)
    p1.draw(g)
    dbgRenderer.render(world, g.getCamera.combined)
    g.drawFPS()
    g.drawSchoolLogo()
  }
}
