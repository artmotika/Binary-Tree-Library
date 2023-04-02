package trees

import BSTIterator
import nodes.BSNode

class BSTree<T : Comparable<T>, S> : AbstractTree<T, S, BSNode<T, S>>() {
    override fun remove(key: T): Boolean {
        val node = findNode(key) ?: return false
        removeNode(node)
        return true
    }

    override fun insert(key: T, value: S): Boolean {
        var parent: BSNode<T, S>? = null
        var currentNode: BSNode<T, S>? = root
        while (currentNode != null) {
            parent = currentNode
            when {
                key < currentNode.key -> currentNode = currentNode.left
                key > currentNode.key -> currentNode = currentNode.right
                else -> {
                    currentNode.value = value
                    return true
                }
            }
        }
        when {
            parent == null -> {
                root = BSNode(key, value)
                return true
            }
            key < parent.key -> {
                parent.left = BSNode(key, value)
                parent.left?.parent = parent
            }
            else -> {
                parent.right = BSNode(key, value)
                parent.right?.parent = parent
            }
        }
        return true
    }

    override fun iterator(): BSTIterator<T, S, BSNode<T, S>> =
        BSTIterator(root)

    private fun transplant(nodeParent: BSNode<T, S>?, nodeChild: BSNode<T, S>?) {
        when {
            nodeParent?.parent == null -> root = nodeChild
            nodeParent == nodeParent.parent?.left -> nodeParent.parent?.left = nodeChild
            else -> nodeParent.parent?.right = nodeChild
        }
        if (nodeChild != null) nodeChild.parent = nodeParent?.parent
    }

    private fun removeNode(node: BSNode<T, S>) {
        when {
            node.left == null -> transplant(node, node.right)
            node.right == null -> transplant(node, node.left)
            else -> {
                val nodeRightNoChild = getMinNode(node.right!!)!!
                if (nodeRightNoChild.parent != node) {
                    transplant(nodeRightNoChild, nodeRightNoChild.right)
                    nodeRightNoChild.right = node.right
                    nodeRightNoChild.right?.parent = nodeRightNoChild
                }
                transplant(node, nodeRightNoChild)
                nodeRightNoChild.left = node.left
                nodeRightNoChild.left?.parent = nodeRightNoChild
            }
        }
    }
}
