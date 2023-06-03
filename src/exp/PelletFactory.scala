package exp

import scala.collection.mutable.ArrayBuffer

class PelletFactory {
    var triangleBox: ArrayBuffer[trianglePellet] = new ArrayBuffer[trianglePellet]()
    var squareBox: ArrayBuffer[squarePellet] = new ArrayBuffer[squarePellet]()
    var hexagonBox: ArrayBuffer[hexagonPellet] = new ArrayBuffer[hexagonPellet]()
    var bigHexaBox: ArrayBuffer[bigHexaPellet] = new ArrayBuffer[bigHexaPellet]()

    val NBR_TRIANGLES = 60
    val NBR_SQUARES = 40
    val NBR_HEXAGONS = 15
    val NBR_BIGHEXAS = 3

    var lastTriangle: Long = 0
    var lastSquare: Long = 0
    var lastHexagon: Long = 0
    var lastBigHexa: Long = 0

    def pelletUpdate(): Unit ={
        //pellet triangulaire
        if(triangleBox.length < NBR_TRIANGLES){
            if(System.nanoTime() > lastTriangle + 10000000){
                lastTriangle = System.nanoTime()

            }
        }
    }


    def spawnTriangle(): Unit ={

    }

    def spawnSquare(): Unit ={

    }

    def spawnHexagon(): Unit ={

    }

    def spawnBigHexa(): Unit = {

    }
}
