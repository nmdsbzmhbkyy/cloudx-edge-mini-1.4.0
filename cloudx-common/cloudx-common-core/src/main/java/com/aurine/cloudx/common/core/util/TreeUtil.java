package com.aurine.cloudx.common.core.util;

import com.aurine.cloudx.common.core.entity.TreeNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Title: TreeUtil
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/20 17:59
 */
public class TreeUtil {
    private TreeUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public static <T extends TreeNode> List<T> build(List<T> treeNodes, Object root) {

        List<T> trees = new ArrayList<>();

        for (T treeNode : treeNodes) {

            if (root.equals(treeNode.getParentId())) {
                trees.add(treeNode);
            }
            for (T it : treeNodes) {
                if (treeNode.getId().equals(it.getParentId())) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.add(it);
                }
            }
        }
        return trees;
    }

    /**
     * 两层循环实现建树
     *
     * @param treeNodes 传入的树节点列表
     * @return
     */
    public static <T extends TreeNode> List<T> build(List<T> treeNodes, Object root, Function<T, Boolean> function) {

        List<T> trees = new ArrayList<>();

        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(treeNode);
            }

            for (T it : treeNodes) {
                if (treeNode.getId().equals(it.getParentId())) {
                    if (treeNode.getChildren() == null) {
                        treeNode.setChildren(new ArrayList<>());
                    }
                    treeNode.add(it);
                }
            }

        }
        resetTrees(trees, function);
        return trees;
    }

    private static <T extends TreeNode> boolean resetTrees(List<T> trees, Function<T, Boolean> function) {


        int size = trees.size();
        int count = 0;
        for (int i = size - 1; i >= 0; i--) {
            T node = trees.get(i);
            if (!function.apply(node)) {
                if (node.getChildren() != null && node.getChildren().size() > 0) {
                    if (resetTrees(node.getChildren(), function)) {
                        trees.remove(i);
                    }else {
                        count++;
                    }
                } else {
                    trees.remove(i);
                }
            } else {
                count++;
            }
        }
        return count == 0;
    }

    /**
     * 使用递归方法建树
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
        List<T> trees = new ArrayList<T>();
        for (T treeNode : treeNodes) {
            if (root.equals(treeNode.getParentId())) {
                trees.add(findChildren(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param treeNodes
     * @return
     */
    public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
        for (T it : treeNodes) {
            if (treeNode.getId() == it.getParentId()) {
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.add(findChildren(it, treeNodes));
            }
        }
        return treeNode;
    }
}
