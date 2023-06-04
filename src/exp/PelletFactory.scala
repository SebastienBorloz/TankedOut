package exp

import com.badlogic.gdx.math.Vector2

import scala.collection.mutable.ArrayBuffer

class PelletFactory {
    var triangleStash: ArrayBuffer[trianglePellet] = new ArrayBuffer[trianglePellet]()
    var squareStash: ArrayBuffer[squarePellet] = new ArrayBuffer[squarePellet]()
    var hexagonStash: ArrayBuffer[hexagonPellet] = new ArrayBuffer[hexagonPellet]()
    var bigHexaStash: ArrayBuffer[bigHexaPellet] = new ArrayBuffer[bigHexaPellet]()

    val NBR_TRIANGLES = 10
    val NBR_SQUARES = 5
    val NBR_HEXAGONS = 3
    val NBR_BIGHEXAS = 1

    var lastTriangle: Long = 0
    var lastSquare: Long = 0
    var lastHexagon: Long = 0
    var lastBigHexa: Long = 0

    def pelletUpdate(): Unit ={
        //pellet triangulaire
        if(triangleStash.length < NBR_TRIANGLES) {
            if (System.currentTimeMillis() > lastTriangle + 1000) {
                lastTriangle = System.currentTimeMillis()
                spawnTriangle()
            }
        }

        if(squareStash.length < NBR_SQUARES) {
            if (System.currentTimeMillis() > lastSquare + 5000) {
                lastSquare = System.currentTimeMillis()
                spawnSquare()
            }
        }

        if(hexagonStash.length < NBR_HEXAGONS) {
            if (System.currentTimeMillis() > lastHexagon + 10000) {
                lastHexagon = System.currentTimeMillis()
                spawnHexagon()
            }
        }

        if(bigHexaStash.length < NBR_BIGHEXAS) {
            if (System.currentTimeMillis() > lastBigHexa + 30000) {
                lastBigHexa = System.currentTimeMillis()
                spawnBigHexa()
            }
        }
    }


    def spawnTriangle(): Unit ={
        println("triangle flag!")
        //TODO: creation du point d'apparition et stockage dans spawnpoint
        var spawnPoint: Vector2 = new Vector2(50,50)
        triangleStash.addOne(new trianglePellet(spawnPoint))
    }

    def spawnSquare(): Unit ={
        println("square flag!")
        //TODO: creation du point d'apparition et stockage dans spawnpoint
        var spawnPoint: Vector2 = new Vector2(50, 50)
        squareStash.addOne(new squarePellet(spawnPoint))
    }

    def spawnHexagon(): Unit ={
        println("hexagon flag!")
        //TODO: creation du point d'apparition et stockage dans spawnpoint
        var spawnPoint: Vector2 = new Vector2(50, 50)
        hexagonStash.addOne(new hexagonPellet(spawnPoint))
    }

    def spawnBigHexa(): Unit = {
        println("bigHexa flag!")
        //TODO: creation du point d'apparition et stockage dans spawnpoint
        var spawnPoint: Vector2 = new Vector2(50, 50)
        bigHexaStash.addOne(new bigHexaPellet(spawnPoint))
    }
}
