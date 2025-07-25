package com.github.kuangcp.string;

import java.util.*;

/**
 * @author Kuangcp
 * 2025-07-07 10:24
 */
public class ACMatcher {

    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        boolean isEnd = false;
        String word = null;
        Node fail = null;
    }

    private final Node root = new Node();

    // 构建Trie树并添加失败指针
    public void buildTrie(Set<String> dictionary) {
        if (dictionary == null || dictionary.isEmpty()) {
            return;
        }

        // 构建Trie树
        for (String word : dictionary) {
            if (word == null || word.isEmpty()) {
                continue;
            }
            Node current = root;
            for (char c : word.toCharArray()) {
                current.children.putIfAbsent(c, new Node());
                current = current.children.get(c);
            }
            current.isEnd = true;
            current.word = word;
        }

        // 构建失败指针
        Queue<Node> queue = new LinkedList<>();
        root.fail = root;
        
        // 将第一层节点的失败指针指向root
        for (Map.Entry<Character, Node> entry : root.children.entrySet()) {
            Node child = entry.getValue();
            child.fail = root;
            queue.offer(child);
        }

        // 构建其他节点的失败指针
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current == null) {
                continue;
            }

            for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                char ch = entry.getKey();
                Node child = entry.getValue();
                queue.offer(child);

                // 从父节点的失败指针开始查找
                Node failNode = current.fail;
                while (failNode != null && !failNode.children.containsKey(ch)) {
                    failNode = failNode.fail;
                }
                
                child.fail = (failNode != null && failNode.children.containsKey(ch)) 
                    ? failNode.children.get(ch) 
                    : root;
            }
        }
    }

    // 查找所有匹配位置
    public List<Match> findMatches(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }

        List<Match> matches = new ArrayList<>();
        Node current = root;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);

            while (current != root && !current.children.containsKey(ch)) {
                current = current.fail;
            }

            current = current.children.getOrDefault(ch, root);

            // 检查当前节点及其失败指针
            Node temp = current;
            while (temp != root) {
                if (temp.isEnd) {
                    int start = i - temp.word.length() + 1;
                    // 检查是否是更长词的一部分
                    if (!isPartOfLongerWord(text, start, temp.word)) {
                        matches.add(new Match(start, i + 1, temp.word));
                    }
                }
                temp = temp.fail;
            }
        }
        return matches;
    }

    // 检查是否是更长词的一部分
    private boolean isPartOfLongerWord(String text, int start, String word) {
        // 检查前后是否有其他汉字
        if (start > 0 && isChineseChar(text.charAt(start - 1))) {
            return true;
        }
        int end = start + word.length();
        if (end < text.length() && isChineseChar(text.charAt(end))) {
            return true;
        }
        return false;
    }

    private boolean isChineseChar(char c) {
        return Character.UnicodeScript.of(c) == Character.UnicodeScript.HAN;
    }

    public static class Match {
        public final int start;
        public final int end;
        public final String word;

        public Match(int start, int end, String word) {
            this.start = start;
            this.end = end;
            this.word = word;
        }

        @Override
        public String toString() {
            return "Match{" +
                    "word='" + word + '\'' +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}