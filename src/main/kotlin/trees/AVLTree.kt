package trees

import BSTIterator
import nodes.AVLNode

class AVLTree<T : Comparable<T>, S> : AbstractTree<T, S, AVLNode<T, S>>() {

    private fun fixHeight(node: AVLNode<T, S>) {
        val leftHeight = (node.left?.height) ?: 0
        val rightHeight = (node.right?.height) ?: 0
        node.height = maxOf(leftHeight, rightHeight) + 1
    }

    private fun AVLNode<T, S>.smallRotateLeft() {
        val right = this.right ?: return
        val parent = this.parent
        right.left?.parent = this
        this.right = right.left
        right.left = this
        when (this) {
            parent?.left -> parent.left = right
            parent?.right -> parent.right = right
        }
        this.parent = right
        right.parent = parent
        fixHeight(this)
        fixHeight(right)
    }

    private fun AVLNode<T, S>.smallRotateRight() {
        val left = this.left ?: return
        val parent = this.parent
        left.right?.parent = this
        this.left = left.right
        left.right = this
        when (this) {
            parent?.left -> parent.left = left
            parent?.right -> parent.right = left
        }
        this.parent = left
        left.parent = parent
        fixHeight(this)
        fixHeight(left)
    }

    private fun AVLNode<T, S>.bigRotateLeft() {
        val right = this.right
        right?.smallRotateRight()
        this.smallRotateLeft()
    }

    private fun AVLNode<T, S>.bigRotateRight() {
        val left = this.left
        left?.smallRotateLeft()
        this.smallRotateRight()
    }

    private fun fixBalance(node: AVLNode<T, S>) {
        fixHeight(node)
        if (node.getBalance() == -2) {
            if (node.right?.getBalance() ?: 0 > 0)
                node.bigRotateLeft()
            else
                node.smallRotateLeft()
        }

        if (node.getBalance() == 2) {
            if (node.left?.getBalance() ?: 0 < 0) {
                node.bigRotateRight()
            } else
                node.smallRotateRight()
        }
    }

    private fun fixup(node: AVLNode<T, S>?) {
        var currentNode = node

        while (currentNode != null) {
            fixHeight(currentNode)
            if (currentNode.getBalance() == -2 || currentNode.getBalance() == 2)
                fixBalance(currentNode)
            if (currentNode.parent == null)
                root = currentNode
            currentNode = currentNode.parent
        }
    }

    private fun removeNode(node: AVLNode<T, S>) {
        val previous = getMaxNode(node.left)

        when {
            (node.right != null && node.left != null) -> {
                node.key = previous!!.key
                node.value = previous.value
                removeNode(previous)
                return
            }

            (node.right == null && node.left != null) -> {
                node.key = node.left!!.key
                node.value = node.left!!.value
                node.right = node.left!!.right
                node.left = node.left!!.left
                fixup(node)
                return
            }

            (node.right != null && node.left == null) -> {
                node.key = node.right!!.key
                node.value = node.right!!.value
                node.left = node.right!!.left
                node.right = node.right!!.right
                fixup(node)
                return
            }

            (node == root) -> {
                root = null
                return
            }

            else -> {
                if (node.key <= node.parent!!.key) {
                    node.parent!!.left = null
                } else {
                    node.parent!!.right = null
                }
                fixup(node.parent)
                return
            }
        }
    }

    override fun remove(key: T): Boolean {
        val node = findNode(key) ?: return false
        removeNode(node)
        return true
    }

    override fun insert(key: T, value: S): Boolean {
        var parent: AVLNode<T, S>? = null
        var currentNode: AVLNode<T, S>? = root
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

    private fun insertWithGivenParent(parent: AVLNode<T, S>?, key: T, value: S) {
        when {
            parent == null -> {
                root = AVLNode(key, value)
            }
            key < parent.key -> {
                parent.left = AVLNode(key, value)
                parent.left?.parent = parent
                fixup(parent.left)
            }
            else -> {
                parent.right = AVLNode(key, value)
                parent.right?.parent = parent
                fixup(parent.right)
            }
        }
    }

    override fun iterator(): BSTIterator<T, S, AVLNode<T, S>> =
        BSTIterator(root)
}
