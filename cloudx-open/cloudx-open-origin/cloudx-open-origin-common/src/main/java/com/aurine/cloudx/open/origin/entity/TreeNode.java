package com.aurine.cloudx.open.origin.entity;


import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: TreeNode
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/20 17:49
 */
@EqualsAndHashCode
public class TreeNode {
    protected String id;
    protected String parentId;
    protected List children = new ArrayList();

    public TreeNode() {
    }

    public void add(TreeNode node) {
        this.children.add(node);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List getChildren() {
        return this.children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TreeNode)) {
            return false;
        } else {
            TreeNode other = (TreeNode) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getId() != other.getId()) {
                return false;
            } else if (this.getParentId() != other.getParentId()) {
                return false;
            } else {
                Object this$children = this.getChildren();
                Object other$children = other.getChildren();
                if (this$children == null) {
                    return other$children == null;
                } else {
                    return this$children.equals(other$children);
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof TreeNode;
    }
}
