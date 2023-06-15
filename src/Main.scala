package ch.hevs.gdx2d

import composants.Joueur
import ch.hevs.gdx2d.components.physics.utils.PhysicsScreenBoundaries
import ch.hevs.gdx2d.desktop.physics.DebugRenderer
import ch.hevs.gdx2d.lib.physics.PhysicsWorld
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ch.hevs.gdx2d.lib.GdxGraphics
import ch.hevs.gdx2d.desktop.PortableApplication
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import com.badlogic.gdx.math.{Vector2, Vector3}
import exp.PelletFactory
import setup.settings


/**
 * TankedOut
 * @author Brocard Sébastien
 * @author Duc Jeremy
 *
 */
object Main {
    def main(args: Array[String]): Unit = {
        new Main
    }
}

class Main extends PortableApplication(2000, 1000) {
    private var dbgRenderer: DebugRenderer = null
    private val world = PhysicsWorld.getInstance
    private var zoom = .0
    private var p1: Joueur = null
    private var polyGen: PelletFactory = new PelletFactory()
    private var prevParam: Int = 0
    private var policeTextes: BitmapFont = null

    override def onInit(): Unit = { // No gravity in this world
        world.setGravity(new Vector2(0, 0))
        setTitle("Test map")

        dbgRenderer = new DebugRenderer

        // Create the obstacles in the scene
        new PhysicsScreenBoundaries(settings.BOX_WIDTH, settings.BOX_HEIGHT)

        p1 = new Joueur(polyGen,30, new Vector2(200, 200), 0)
        zoom = 2
        polyGen.pelletInit()
    }

    override def onGraphicRender(g: GdxGraphics): Unit = {
        //println(polyGen.triangleStash.length, polyGen.squareStash.length, polyGen.pentagonStash.length, polyGen.bigPentaStash.length)
        val playerPosition = p1.getPos
        g.clear()
        for(i <- settings.BOX_WIDTH/10 until settings.BOX_WIDTH by settings.BOX_WIDTH/10){
            g.drawLine(i, 0, i, settings.BOX_HEIGHT, Color.DARK_GRAY)
        }
        for (i <- settings.BOX_WIDTH/10 until settings.BOX_HEIGHT by settings.BOX_WIDTH/10) {
            g.drawLine(0, i, settings.BOX_WIDTH, i, Color.DARK_GRAY)
        }
        g.drawString(playerPosition.x, playerPosition.y - 40, p1.exp.toString)
        // Physics update
        polyGen.pelletUpdate()
        PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime)
        // Camera follows the hero
        g.zoom(zoom.toFloat)
        g.moveCamera((playerPosition.x - getWindowWidth / 2 * zoom).toFloat, (playerPosition.y - getWindowHeight / 2 * zoom).toFloat)
        /**
         * Move the player according to key presses
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

        if (Gdx.input.isKeyPressed(Input.Keys.E)) p1.playerBox.applyBodyAngularImpulse(0.5f,false)

        p1.update(Gdx.graphics.getDeltaTime)
        p1.draw(g)
        dbgRenderer.render(world, g.getCamera.combined)
        val mouseX = Gdx.input.getX()
        val mouseY = Gdx.input.getY()
        val posMouse: Vector2 = new Vector2(mouseX.toFloat + 1, mouseY.toFloat + 1)
        val truePosMouse = g.getCamera.unproject(new Vector3(posMouse.x, posMouse.y, 0))
        val angle: Float = p1.getAngle(new Vector2(truePosMouse.x,truePosMouse.y), playerPosition).toFloat
        p1.mouseAngle = angle
        if(angle < 180) {
            g.drawFilledRectangle(playerPosition.x + 30 * math.sin(angle * math.Pi / 180.0).toFloat, playerPosition.y - 30 * math.cos(angle * math.Pi / 180.0).toFloat, 21, 33, angle, Color.DARK_GRAY)
        }else{
            g.drawFilledRectangle(playerPosition.x + 30 * math.sin(angle * math.Pi / 180.0).toFloat, playerPosition.y + 30 * math.cos(angle * math.Pi / 180.0).toFloat, 21, 33, 180 - angle, Color.DARK_GRAY)
        }


        //dessin hud level up
        val posRefXp: Vector2 = new Vector2(playerPosition.x - 800 * zoom.toFloat, playerPosition.y - 400 * zoom.toFloat)
        for(i <- 0 to 5){
            g.drawRectangle(posRefXp.x, posRefXp.y + 100 * i * zoom.toFloat, 300 * zoom.toFloat, 100 * zoom.toFloat, 0)
        }

        val colorTab: Array[Color] = Array(Color.FOREST, Color.GOLD, Color.CYAN, Color.YELLOW, Color.RED, Color.PINK)
        val playerStats: Array[Int] = Array(p1.stats.regen, p1.stats.maxHealth, p1.stats.bulletSpeed, p1.stats.bulletDamage, p1.stats.reload, p1.stats.movementSpeed)
        for(j <- 0 to 5){
            for (i <- 0 until playerStats(j)) {
                g.drawFilledRectangle(posRefXp.x - 140 * zoom.toFloat + 40 * i * zoom.toFloat, posRefXp.y + j * 100 * zoom.toFloat, 20 * zoom.toFloat, 100 * zoom.toFloat, 0, colorTab(j))
            }
        }

        if((40.0 * zoom).toInt != prevParam) {
            val optimusF: FileHandle = Gdx.files.internal("data/font/Timeless.ttf")
            val generator: FreeTypeFontGenerator = new FreeTypeFontGenerator(optimusF)
            val parameter: FreeTypeFontParameter = new FreeTypeFontParameter()
            prevParam = (40.0 * zoom).toInt
            parameter.size = generator.scaleForPixelHeight(prevParam)
            parameter.color = Color.WHITE
            policeTextes = generator.generateFont(parameter)
        }
        
        val strStats: Array[String] = Array("regen (1)", "maxHealth (2)", "bulletSpeed (3)", "bulletDamage (4)", "reload (5)", "movementSpeed (6)")
        for(i <- 0 to 5){
            g.drawString(posRefXp.x - 150 * zoom.toFloat, posRefXp.y + 10 * zoom.toFloat + 100 * i * zoom.toFloat, s"${strStats(i)}", policeTextes)
        }



        g.drawFPS()
        g.drawSchoolLogo()
    }

    override def onClick(x: Int, y: Int, button: Int): Unit = {
        p1.shooting = true
    }

    override def onRelease(x: Int, y: Int, button: Int): Unit = {
        p1.shooting = false
    }
}
