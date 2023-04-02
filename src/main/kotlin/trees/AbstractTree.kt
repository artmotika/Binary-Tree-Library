package trees

import nodes.AbstractNode

abstract class AbstractTree<T : Comparable<T>, S, N : AbstractNode<T, S, N>> : Iterable<N> {
    protected var root: N? = null

    protected tailrec fun findNode(key: T, currentNode: N? = this.root): N? {
        return when {
            currentNode == null -> null
            key < currentNode.key -> findNode(key, currentNode.left)
            key > currentNode.key -> findNode(key, currentNode.right)
            else -> currentNode
        }
    }

    protected tailrec fun getMaxNode(sourceNode: N?): N? {
        return when (sourceNode?.right) {
            null -> sourceNode
            else -> getMaxNode(sourceNode.right)
        }
    }

    protected tailrec fun getMinNode(sourceNode: N?): N? {
        return when (sourceNode?.left) {
            null -> sourceNode
            else -> getMinNode(sourceNode.left)
        }
    }

    fun find(key: T) = findNode(key)?.value

    abstract fun remove(key: T): Boolean

    abstract fun insert(key: T, value: S): Boolean
}
