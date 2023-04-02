package trees

import BSTIterator
import nodes.RBNode
import nodes.RBNode.Color

class RBTree<T : Comparable<T>, S> : AbstractTree<T, S, RBNode<T, S>>() {

    private fun RBNode<T, S>.swapColorsWith(nodeToSwap: RBNode<T, S>?) {
        nodeToSwap?.let {
            this.color = nodeToSwap.color.also {
                nodeToSwap.color = this.color
            }
        }
    }

    private fun RBNode<T, S>.rotateLeft() {
        val right = this.right ?: return
        val parent = this.parent

        this.swapColorsWith(right)

        right.left?.parent = this
        this.right = right.left
        right.left = this

        when (this) {
            parent?.left -> parent.left = right
            parent?.right -> parent.right = right
        }

        this.parent = right
        right.parent = parent
    }

    private fun RBNode<T, S>.rotateRight() {
        val left = this.left ?: return
        val parent = this.parent

        this.swapColorsWith(left)

        left.right?.parent = this
        this.left = left.right
        left.right = this

        when (this) {
            parent?.left -> parent.left = left
            parent?.right -> parent.right = left
        }

        this.parent = left
        left.parent = parent
    }

    private fun RBNode<T, S>?.isBlackOrNull() =
        this == null || this.color == Color.Black

    override fun insert(key: T, value: S): Boolean {
        var parent: RBNode<T, S>? = null
        var currentNode: RBNode<T, S>? = root

        while (currentNode != null) {
            parent = currentNode

            when {
                key < currentNode.key -> currentNode = currentNode.left
                key > currentNode.key -> currentNode = currentNode.right
                key == currentNode.key -> {
                    currentNode.value = value
                    return true
                }
            }
        }

        insertWithGivenParent(parent, key, value)
        return true
    }

    private fun insertWithGivenParent(parent: RBNode<T, S>?, key: T, value: S) {

        when {
            parent == null -> {
                root = RBNode(key, value)
                return
            }

            key < parent.key -> {
                parent.left = RBNode(key, value)
                parent.left?.color = Color.Red
                parent.left?.parent = parent
                insertFixup(parent.left)
            }

            else -> {
                parent.right = RBNode(key, value)
                parent.right?.color = Color.Red
                parent.right?.parent = parent
                insertFixup(parent.right)
            }
        }

        return
    }

    private fun insertFixup(node: RBNode<T, S>?) {
        var currentNode: RBNode<T, S>? = node
        var uncle: RBNode<T, S>?

        while (!currentNode?.parent.isBlackOrNull()) {
            if (currentNode!!.parent == currentNode.parent?.parent?.left) {
                uncle = currentNode.parent?.parent?.right

                when {
                    !uncle.isBlackOrNull() -> {
                        currentNode.parent?.color = Color.Black
                        uncle!!.color = Color.Black
                        currentNode.parent?.parent?.color = Color.Red
                        currentNode = currentNode.parent?.parent
                    }

                    currentNode == currentNode.parent?.right -> {
                        currentNode = currentNode.parent

                        currentNode!!.parent?.parent ?: run { root = currentNode!!.parent }
                        currentNode.rotateLeft()
                    }

                    currentNode == currentNode.parent?.left -> {
                        currentNode.parent?.parent?.parent ?: run { root = currentNode!!.parent }
                        currentNode.parent?.parent?.rotateRight()
                    }
                }

            } else {
                uncle = currentNode.parent?.parent?.left

                when {
                    !uncle.isBlackOrNull() -> {
                        currentNode.parent?.color = Color.Black
                        uncle!!.color = Color.Black
                        currentNode.parent?.parent?.color = Color.Red
                        currentNode = currentNode.parent?.parent
                    }

                    currentNode == currentNode.parent?.left -> {
                        currentNode = currentNode.parent

                        currentNode!!.parent?.parent ?: run { root = currentNode.parent }
                        currentNode.rotateRight()
                    }

                    currentNode == currentNode.parent?.right -> {
                        currentNode.parent?.parent?.parent ?: run { root = currentNode.parent }
                        currentNode.parent?.parent?.rotateLeft()
                    }
                }
            }
        }

        root?.color = Color.Black
    }

    override fun remove(key: T): Boolean {
        val node = findNode(key) ?: return false

        removeNode(node)

        return true
    }

    private fun removeNode(node: RBNode<T, S>) {
        val previous = getMaxNode(node.left)

        when {
            node.right != null && node.left != null -> {
                node.key = previous!!.key
                node.value = previous.value
                removeNode(previous)
                return
            }

            node == root && node.isLeaf() -> {
                root = null
                return
            }

            !node.isBlackOrNull() && node.isLeaf() -> {

                when (node) {
                    node.parent!!.left -> node.parent!!.left = null
                    else -> node.parent!!.right = null
                }

                return
            }

            node.isBlackOrNull() && node.left != null && !node.left.isBlackOrNull() -> {
                node.key = node.left!!.key
                node.value = node.left!!.value
                node.left = null
                return
            }

            node.isBlackOrNull() && node.right != null && !node.right.isBlackOrNull() -> {
                node.key = node.right!!.key
                node.value = node.right!!.value
                node.right = null
                return
            }

            else -> removeFixup(node)
        }

        when (node) {
            node.parent!!.left -> node.parent!!.left = null
            else -> node.parent!!.right = null
        }
    }

    private fun removeFixup(node: RBNode<T, S>) {
        var brother: RBNode<T, S>?

        if (node.parent != null) {
            brother = node.brother!!

            if (!brother.isBlackOrNull()) {

                when (node) {
                    node.parent!!.left -> node.parent!!.rotateLeft()
                    node.parent!!.right -> node.parent!!.rotateRight()
                }

                if (node.parent == root)
                    root = node.parent!!.parent
            }

            brother = node.brother!!

            if (brother.isBlackOrNull() && brother.left.isBlackOrNull() &&
                brother.right.isBlackOrNull() && node.parent.isBlackOrNull()
            ) {

                brother.color = Color.Red
                removeFixup(node.parent!!)

            } else {

                brother = node.brother!!

                if (brother.isBlackOrNull() && brother.left.isBlackOrNull() &&
                    brother.right.isBlackOrNull() && !node.parent.isBlackOrNull()
                ) {
                    brother.color = Color.Red
                    node.parent!!.color = Color.Black

                } else {
                    brother = node.brother!!

                    if (brother.isBlackOrNull())

                        when {
                            !brother.left.isBlackOrNull() && brother.right.isBlackOrNull() &&
                                    node == node.parent?.left -> brother.rotateRight()

                            !brother.right.isBlackOrNull() && brother.left.isBlackOrNull() &&
                                    node == node.parent?.right -> brother.rotateLeft()
                        }

                    brother = node.brother

                    when (node) {
                        node.parent!!.left -> {
                            brother?.right?.color = Color.Black

                            node.parent?.rotateLeft()
                        }

                        else -> {
                            brother?.left?.color = Color.Black

                            node.parent?.rotateRight()
                        }
                    }

                    if (root == node.parent)
                        root = node.parent!!.parent
                }
            }
        }
    }

    override fun iterator(): BSTIterator<T, S, RBNode<T, S>> =
        BSTIterator(root)
}
