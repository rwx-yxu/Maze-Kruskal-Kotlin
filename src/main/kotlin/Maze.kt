import org.openrndr.draw.Drawer

class Maze(cols: Double, rows: Double){
    private val nodes = mutableListOf<Node>() //16
    private val edges = mutableListOf<MutableList<Node>>()
    private val sets = mutableMapOf<Int,MutableList<Node>>()

    companion object {
        fun nodeIndex(i:Int, j:Int, cols:Int, rows: Int): Int{
            //validation for edge cases will return a null index
            if (i < 0 || j < 0 || i > cols-1 || j > rows -1){
                return -1
            }
            return i + j * cols
        }
    }

    init{
        //Generate array list of nodes objects
        for (j in 0 until rows.toInt()) {
            for (i in 0 until cols.toInt()) {

                val node = Node(i, j, nodeIndex(i, j, cols.toInt(), rows.toInt()))
                nodes.add(node)

                sets[node.nodeIndex] = mutableListOf(node)
            }
        }

        for (i in nodes.indices){
            val nodeNeighbours = nodes[i].getNeighbours(cols = cols.toInt(), rows = rows.toInt(), nodes = nodes)
            for (j in nodeNeighbours.indices){
                val edgeNodes = mutableListOf<Node>()
                edgeNodes.add(nodes[i])
                edgeNodes.add(nodeNeighbours[j])
                edges.add(edgeNodes)
            }
            nodes[i].visited = true
        }

        edges.shuffle()
    }

    fun show(drawer: Drawer, scale: Double) {
        if (edges.isNotEmpty()){
            val index = edges.size -1

            setUnion(edges[index])
            edges.removeAt(index)
        }
        nodes.forEach{
            it.show(drawer, scale)
        }
    }

    private fun setUnion(set :List<Node>){
        if(set[0].setKey != set[1].setKey){
            val setSizeA = sets[set[0].setKey]?.size
            val setSizeB = sets[set[1].setKey]?.size

            if (setSizeA != null && setSizeB != null){
                val setSizeResult = setSizeA.compareTo(setSizeB)
                if (setSizeResult > 0){
                    union(set[1].setKey, set[0].setKey)
                }else if (setSizeResult < 0){
                    union(set[0].setKey, set[1].setKey)
                }else{
                    if (set[0].setKey > set[1].setKey){
                        union(set[1].setKey, set[0].setKey)
                    }else {
                        union(set[0].setKey, set[1].setKey)
                    }
                }
                set[0].removeWall(set[1])
            }
        }
    }

    private fun union(key: Int,  targetNewKey: Int){
        sets[key]?.forEach {
            it.setKey = targetNewKey
            sets[it.setKey]?.add(it)
        }
        sets.remove(key)
    }
}