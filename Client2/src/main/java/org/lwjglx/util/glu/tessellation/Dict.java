package org.lwjglx.util.glu.tessellation;

class Dict {

    DictNode head;
    Object frame;
    DictLeq leq;

    private Dict() {}

    static Dict dictNewDict(Object frame, DictLeq leq) {
        Dict dict = new Dict();
        dict.head = new DictNode();

        dict.head.key = null;
        dict.head.next = dict.head;
        dict.head.prev = dict.head;

        dict.frame = frame;
        dict.leq = leq;

        return dict;
    }

    static void dictDeleteDict(Dict dict) {
        dict.head = null;
        dict.frame = null;
        dict.leq = null;
    }

    static DictNode dictInsert(Dict dict, Object key) {
        return dictInsertBefore(dict, dict.head, key);
    }

    static DictNode dictInsertBefore(Dict dict, DictNode node, Object key) {
        do {
            node = node.prev;
        } while (node.key != null && !dict.leq.leq(dict.frame, node.key, key));

        DictNode newNode = new DictNode();
        newNode.key = key;
        newNode.next = node.next;
        node.next.prev = newNode;
        newNode.prev = node;
        node.next = newNode;

        return newNode;
    }

    static Object dictKey(DictNode aNode) {
        return aNode.key;
    }

    static DictNode dictSucc(DictNode aNode) {
        return aNode.next;
    }

    static DictNode dictPred(DictNode aNode) {
        return aNode.prev;
    }

    static DictNode dictMin(Dict aDict) {
        return aDict.head.next;
    }

    static void dictDelete(DictNode node) {
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }

    static DictNode dictSearch(Dict dict, Object key) {
        DictNode node = dict.head;

        do {
            node = node.next;
        } while (node.key != null && !(dict.leq.leq(dict.frame, key, node.key)));

        return node;
    }

    public interface DictLeq {

        boolean leq(Object frame, Object key1, Object key2);
    }
}
