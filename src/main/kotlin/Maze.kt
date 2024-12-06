import org.openrndr.draw.Drawer

class Maze(cols: Double, rows: Double){
    private val nodes = mutableListOf<Node>()
    private val edges = mutableListOf<NodePair>()
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
                edges.add(NodePair(left = nodes[i], right = nodeNeighbours[j]))
            }
            nodes[i].visited = true
        }

        edges.shuffle()
    }

    fun show(drawer: Drawer, scale: Double) {
        if (edges.isNotEmpty()){
            val index = edges.size -1

            setUnionEdge(edges[index].left, edges[index].right)
            edges.removeAt(index)
        }
        nodes.forEach{
            it.show(drawer, scale)
        }
    }

    private fun setUnionEdge(left: Node, right: Node){
        if (left.setKey != right.setKey){
            val setSizeLeft = sets[left.setKey]?.size
            val setSizeRight = sets[right.setKey]?.size

            if (setSizeLeft != null && setSizeRight != null){
                setSizeLeft.compareTo(setSizeRight).let {
                    when {
                        it > 0 -> union(right.setKey, left.setKey)
                        it < 0 -> union(left.setKey, right.setKey)
                        else -> when {
                                left.setKey > right.setKey -> union(right.setKey, left.setKey)
                                else -> union(left.setKey, right.setKey)
                        }
                    }
                }
                left.removeWall(right)
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

data class NodePair(val left: Node, val right: Node)