package com.tridevmc.architecture.legacy.common.utils;

import com.google.common.collect.Lists;
import net.minecraft.world.phys.AABB;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Deprecated
public class LegacyAABBTree<T> {

    private Node theNode;

    public LegacyAABBTree(Collection<T> items, Function<T, AABB> boxGetter) {
        items.forEach(t -> {
            var box = boxGetter.apply(t);
            if (LegacyAABBTree.this.theNode == null) {
                LegacyAABBTree.this.theNode = new Node(box, t);
            } else {
                LegacyAABBTree.this.theNode.addNode(box, t);
            }
        });
    }

    public LegacyAABBTree(AABB startBox, T item) {
        this.theNode = new Node(startBox, item);
    }

    public Node getRoot() {
        return this.theNode;
    }

    public void add(AABB box, T item) {
        this.theNode.addNode(box, item);
    }

    public AABB getBounds() {
        return this.theNode.getValue();
    }

    public List<T> search(AABB box) {
        List<T> out = Lists.newArrayList();
        List<Node> queue = Lists.newArrayList(this.theNode);

        while (!queue.isEmpty()) {
            Node node = queue.remove(0);
            if (box.intersects(node.getValue())) {
                if (node.item == null) {
                    queue.add(node.left);
                    queue.add(node.right);
                } else {
                    out.add(node.item);
                }
            }
        }
        return out;
    }

    @Deprecated
    public class Node {
        private Node left;
        private Node right;
        private AABB value;
        private T item;

        public Node(AABB value, T item) {
            this.left = this.getLeft();
            this.right = this.getRight();
            this.value = value;
            this.item = item;
        }

        public boolean hasChildren() {
            return this.getLeft() != null && this.getRight() != null;
        }

        public AABB getValue() {
            return this.value;
        }

        public Node getLeft() {
            return this.left;
        }

        public Node getRight() {
            return this.right;
        }

        public List<Node> addNode(AABB box, T item) {
            AABB oldValue = this.getValue();
            this.value = this.value.minmax(box);
            if (!this.hasChildren()) {
                double otherVolume = this.calculateVolume(box);
                double thisVolume = this.calculateVolume(oldValue);
                if (thisVolume > otherVolume) {
                    this.left = new Node(box, item);
                    this.right = new Node(oldValue, this.item);
                } else {
                    this.left = new Node(oldValue, this.item);
                    this.right = new Node(box, item);
                }
                this.item = null;
                return Lists.newArrayList(this.getLeft(), this.getRight());
            } else {
                double leftVolume = this.getLeft().calculateVolume(box);
                double rightVolume = this.getRight().calculateVolume(box);

                if (leftVolume > rightVolume) {
                    return this.getLeft().addNode(box, item);
                } else {
                    return this.getRight().addNode(box, item);
                }
            }
        }

        private double calculateVolume(AABB other) {
            var intersection = this.getValue().minmax(other);
            return Math.max(0, intersection.getXsize() * intersection.getYsize() * intersection.getZsize());
        }
    }

}
