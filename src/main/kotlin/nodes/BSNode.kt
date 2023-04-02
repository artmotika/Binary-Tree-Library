package nodes

class BSNode<T : Comparable<T>, S>(override var key: T, override var value: S):
    AbstractNode<T, S, BSNode<T, S>>() {

    override var left: BSNode<T, S>? = null
    override var right: BSNode<T, S>? = null

    var parent: BSNode<T, S>? = null
        internal set
}