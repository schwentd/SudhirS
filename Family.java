package net.telematics.tsp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class Solution {
	public class Value {
		private Integer parentId;
		private Integer nodeId;
		private String nodeName;

		public Value(Integer parentId, Integer nodeId, String nodeName) {
			super();
			this.parentId = parentId;
			this.nodeId = nodeId;
			this.nodeName = nodeName;
		}

		public Integer getParentId() {
			return parentId;
		}

		public Integer getNodeId() {
			return nodeId;
		}

		public String getNodeName() {
			return nodeName;
		}
	}

	public class Node {
		Value val;
		Node left;
		Node right;

		Node() {
		}

		Node(Value val) {
			this.val = val;
		}
	}

	public Node buildFamilyTree(String s) {
		if (s == null || s.length() == 0) {
			return null;
		}

		Map<Integer, List<Node>> holdFamily = new LinkedHashMap<>();
		Node root = buildFamily(s, holdFamily);

		List<Node> nodes = new ArrayList<>();
		nodes.add(root);
		nodes.addAll(holdFamily.values().stream().flatMap(pList -> pList.stream()).collect(Collectors.toList()));

		return levelOrder(nodes, root, 0);
	}

	private Node buildFamily(String s, Map<Integer, List<Node>> holdFamily) {
		Node root = null;
		StringTokenizer token = new StringTokenizer(s, "|");
		
		while (token.hasMoreTokens()) {
			List<Node> nodes = new LinkedList<>();
			String[] nVals = token.nextToken().split(",");
			Integer parentId = nVals[0] != null && !nVals[0].equals("null") ? Integer.parseInt(nVals[0]) : null;
			Integer nodeId = nVals[1] != null ? Integer.parseInt(nVals[1]) : null;

			Node nd = new Node(new Value(parentId, nodeId, nVals[2]));
			if (parentId == null) {
				holdFamily.put(nodeId, nodes);
				root = nd;
			} else if (holdFamily.containsKey(parentId)) {
				holdFamily.get(parentId).add(nd);
				holdFamily.put(nodeId, nodes);
			} 
		}
		return root;
	}

	public Node levelOrder(List<Node> nodes, Node root, int i) {
		if (i < nodes.size()) {
			Node temp = new Node(nodes.get(i).val);
			root = temp;
			root.left = levelOrder(nodes, root.left, 2 * i + 1);
			root.right = levelOrder(nodes, root.right, 2 * i + 2);
		}
		return root;
	}

	public static void main(String[] args) {
		Node root = new Solution().buildFamilyTree(
				"null,0,grandpa|0,1,son|0,2,daugther|1,3,grandkid|1,4,grandkid|2,5,grandkid|5,6,greatgrandkid");
		StringBuilder sb = new StringBuilder();
		traversePreOrder(sb, root);
		System.err.println(sb.toString());
	}

	private static void traversePreOrder(StringBuilder sb, Node node) {
		if (node != null) {
			sb.append(node.val.nodeName);
			sb.append("\n");
			traversePreOrder(sb, node.left);
			traversePreOrder(sb, node.right);
		}
	}

}
