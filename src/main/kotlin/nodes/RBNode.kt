package nodes

class RBNode<T : Comparable<T>, S>(override var key: T, override var value: S) :
    AbstractNode<T, S, RBNode<T, S>>() {

    enum class Color { Red, Black }

    override var left: RBNode<T, S>? = null

    override var right: RBNode<T, S>? = null

    var parent: RBNode<T, S>? = null
        internal set

    var color = Color.Black
        internal set

    val brother: RBNode<T, S>?
        get() = when (this) {
            parent?.left -> parent?.right
            parent?.right -> parent?.left
            else -> null
        }

    internal fun isLeaf() =
        right == null && left == null
}
