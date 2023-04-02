package nodes

class AVLNode<T : Comparable<T>, S>(override var key: T, override var value: S):
    AbstractNode<T, S, AVLNode<T, S>>() {

    override var left: AVLNode<T, S>? = null

    override var right: AVLNode<T, S>? = null

    var parent: AVLNode<T, S>? = null
        internal set

    var height = 1
        internal set

    internal fun getBalance(): Int =
        (left?.height?:0) - (right?.height?:0)
}
