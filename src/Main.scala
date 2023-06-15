package ch.hevs.gdx2d

import ch.hevs.gdx2d.Main.main
import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import composants.{Bot, Joueur}
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
import com.badlogic.gdx.physics.box2d.Body
import exp.PelletFactory
import setup.settings

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


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
    private var b1: Bot = null
    private var polyGen: PelletFactory = new PelletFactory()
    private var prevParam: Int = 0
    private var policeTextes: BitmapFont = null
    private var released: Boolean = true

    override def onInit(): Unit = { // No gravity in this world
        world.setGravity(new Vector2(0, 0))
        setTitle("Tanked Out")



        dbgRenderer = new DebugRenderer
        // Create the obstacles in the scene
        new PhysicsScreenBoundaries(settings.BOX_WIDTH, settings.BOX_HEIGHT)

        p1 = new Joueur(polyGen,30, new Vector2(200, 200), 0)
        //b1 = new Bot(polyGen,30, new Vector2(300,300),0)
        zoom = 2
        polyGen.pelletInit()
    }


    override def onGraphicRender(g: GdxGraphics): Unit = {
        if (Gdx.input.isKeyPressed(Input.Keys.G)) {p1.classy.playerClass = p1.classy.Sniper}
        if (Gdx.input.isKeyPressed(Input.Keys.H)) {p1.classy.playerClass = p1.classy.Canon}
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {p1.classy.playerClass = p1.classy.Doduble}
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {p1.classy.playerClass = p1.classy.MG}

        // mise a jour des pellets
        polyGen.pelletUpdate()
        PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime)
        // la caméra suit le joueur

        // obtention de la position du joueur 1 et centrage de la camera sur lui
        val playerPosition = p1.getPos
        g.zoom(zoom.toFloat)
        g.moveCamera((playerPosition.x - getWindowWidth / 2 * zoom).toFloat, (playerPosition.y - getWindowHeight / 2 * zoom).toFloat)

        //DEBUG OPTION: gestion du zoom avec 'W' et 'S'
        if (Gdx.input.isKeyPressed(Input.Keys.W)) if (zoom > 0.2) zoom -= 0.1
        if (Gdx.input.isKeyPressed(Input.Keys.S)) if (zoom < 5) zoom += 0.1

        //donne les flags au joueur si la croix directionnelle est touchee
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_UP)) p1.moveUp = true
        else p1.moveUp = false
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_DOWN)) p1.moveDown = true
        else p1.moveDown = false
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT)) p1.moveLeft = true
        else p1.moveLeft = false
        if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT)) p1.moveRight = true
        else p1.moveRight = false


        //effacement complet de l'affichage
        g.clear()

        //commence par le grillage pour l'avoir au dernier plan
        for (i <- settings.BOX_WIDTH/10 until settings.BOX_WIDTH by settings.BOX_WIDTH/10) {g.drawLine(i, 0, i, settings.BOX_HEIGHT, Color.DARK_GRAY)}
        for (i <- settings.BOX_WIDTH/10 until settings.BOX_HEIGHT by settings.BOX_WIDTH/10) {g.drawLine(0, i, settings.BOX_WIDTH, i, Color.DARK_GRAY)}

        //mise a jour du modele physique
        PhysicsWorld.updatePhysics(Gdx.graphics.getDeltaTime)

        //mise a jour du joueur et affichage. DEBUG OPTION: on affiche egalement son xp
        p1.update(Gdx.graphics.getDeltaTime)
        p1.draw(g)
        //b1.draw(g)
        dbgRenderer.render(world, g.getCamera.combined)
        g.drawString(playerPosition.x, playerPosition.y - 40, p1.exp.toString)

        //affichage du/des canons
        p1.classy.Render(p1, playerPosition, g)


        //------------- gestion des levels up -------------

        //calcul des niveaux a depenser
        val lvlUtil: Int = p1.stats.regen + p1.stats.maxHealth + p1.stats.bulletSpeed + p1.stats.bulletDamage + p1.stats.reload + p1.stats.movementSpeed - 5

        //si le joueur n'a pas tout depense
        if(lvlUtil != p1.getLevel()) {
            if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)){ //si la touche 1 est enclenchee
                if (p1.stats.movementSpeed < 8 && released) { //et que la stat n'a pas atteint son max
                    released = false
                    p1.stats.movementSpeed += 1 //augmente la vitesse de deplacement
                }

            } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_2)){ //si la touche 2 est enclenchee
                if (p1.stats.reload < 8 && released) { //et que la stat n'a pas atteint son max
                    released = false
                    p1.stats.reload += 1 //augmente la vitesse de rechargement
                }

            } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_3)){ //si la touche 3 est enclenchee
                if (p1.stats.bulletDamage < 8 && released) { //et que la stat n'a pas atteint son max
                    released = false
                    p1.stats.bulletDamage += 1 //augmente les degats des projectiles
                }

            } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_4)){ //si la touche 4 est enclenchee
                if (p1.stats.bulletSpeed < 8 && released) { //et que la stat n'a pas atteint son max
                    released = false
                    p1.stats.bulletSpeed += 1 //augmente la vitesse des projectiles
                }

            } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_5)){ //si la touche 5 est enclenchee
                if (p1.stats.maxHealth < 8 && released) { //et que la stat n'a pas atteint son max
                    released = false
                    p1.stats.maxHealth += 1 //augmente les points de vie max
                }

            } else if (Gdx.input.isKeyPressed(Input.Keys.NUM_6)){ //si la touche 6 est enclenchee
                if (p1.stats.regen < 8 && released) { //et que la stat n'a pas atteint son max
                    released = false
                    p1.stats.regen += 1 //augmente la regeneration de points de vie
                }
            } else {
                //gestion bizarre pour que tout les niveaux partent pas d'un coup en pressant une touche une fois
                released = true
            }

        /** zone de searching and acquiring target pour le bot */
        /*var bodies = new com.badlogic.gdx.utils.Array[Body]
        var botDestination: Vector2 = new Vector2(0,0)
            world.getBodies(bodies)
        for (i <- bodies.toArray) {
            botDestination = b1.getNearestObject(i)
        }
        val direction = b1.getDirection(botDestination, b1.getPos)
        b1.move(b1, b1.getPos, botDestination, direction)*/
            //------------- dessin hud level up -------------

            //calcule la position du coin de l'ecran par rapport au joueur
            val posRefXp: Vector2 = new Vector2(playerPosition.x - 800 * zoom.toFloat, playerPosition.y - 400 * zoom.toFloat)
            //dessine les rectangles de separation des categories
            for (i <- 0 to 5) {
                g.drawRectangle(posRefXp.x, posRefXp.y + 100 * i * zoom.toFloat, 300 * zoom.toFloat, 100 * zoom.toFloat, 0)
            }

            //cree un tableau des couleurs et des stats pour les afficher dans l'ordre facilement
            val colorTab: Array[Color] = Array(Color.FOREST, Color.GOLD, Color.VIOLET, Color.BLUE, Color.BROWN, Color.FIREBRICK)
            val playerStats: Array[Int] = Array(p1.stats.regen, p1.stats.maxHealth, p1.stats.bulletSpeed, p1.stats.bulletDamage, p1.stats.reload, p1.stats.movementSpeed)
            //affiche les stats dans leurs categories respectives avec des barres verticales (entre 1 et 8)
            for (j <- 0 to 5) {
                for (i <- 0 until playerStats(j)) {
                    g.drawFilledRectangle(posRefXp.x - 140 * zoom.toFloat + 40 * i * zoom.toFloat, posRefXp.y + j * 100 * zoom.toFloat, 20 * zoom.toFloat, 100 * zoom.toFloat, 0, colorTab(j))
                }
            }

            //si le zoom a ete modifie depuis la derniere creation de font, en refait une pour adapter la taille du texte
            if ((40.0 * zoom).toInt != prevParam) {
                val optimusF: FileHandle = Gdx.files.internal("data/font/Timeless.ttf")
                val generator: FreeTypeFontGenerator = new FreeTypeFontGenerator(optimusF)
                val parameter: FreeTypeFontParameter = new FreeTypeFontParameter()
                prevParam = (40.0 * zoom).toInt
                parameter.size = generator.scaleForPixelHeight(prevParam)
                parameter.color = Color.WHITE
                policeTextes = generator.generateFont(parameter)
            }

            //affiche le texte des stats par dessus les barres
            val strStats: Array[String] = Array("regen (6)", "maxHealth (5)", "bulletSpeed (4)", "bulletDamage (3)", "reload (2)", "movementSpeed (1)")
            for (i <- 0 to 5) {
                g.drawString(posRefXp.x - 150 * zoom.toFloat, posRefXp.y + 10 * zoom.toFloat + 100 * i * zoom.toFloat, s"${strStats(i)}", policeTextes)
            }
        }

        //dessin FPS et logo de l'ecole
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
