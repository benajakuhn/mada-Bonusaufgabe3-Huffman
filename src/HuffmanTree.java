import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.PriorityQueue;


public class HuffmanTree {
    private HuffmanNode root;

    public HuffmanTree(int[] characters, int[] frequencies) {
        buildHuffmanTree(characters, frequencies);
    }

    public HuffmanTree() {
    }

    // Huffmanbaum anhand der characters und frequencies erstellen
    private void buildHuffmanTree(int[] characters, int[] frequencies) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(characters.length, new NodeComparator());

        for (int i = 0; i < characters.length; i++) {
            HuffmanNode node = new HuffmanNode(characters[i], frequencies[i]);
            priorityQueue.add(node);
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();

            HuffmanNode newNode = new HuffmanNode(0, left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;

            priorityQueue.add(newNode);
        }
        root = priorityQueue.poll();
    }

    //Kodiereungstabelle für tree erstellen
    public String generateCodingTable(HuffmanTree tree, int[] chars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            sb.append(chars[i]);
            sb.append(":");
            sb.append(genCode(tree.root, "", chars[i]));
            if (i < chars.length - 1) {
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public String encode(HuffmanTree tree, int[] chars) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            sb.append(genCode(tree.root, "", chars[i]));
        }
        return sb.toString();
    }

    private String genCode(HuffmanNode node, String code, int value) {
        if (node == null) {
            return null;
        } else if (node.character == value) {
            return code;
        } else {
            String left = genCode(node.left, code + "0", value);
            String right = genCode(node.right, code + "1", value);
            if (left != null) {
                return left;
            } else {
                return right;
            }
        }
    }

    // lesen einer Kodierungstabelle und diese als Huffmanbaum darstellen
    public HuffmanTree readCodingTable(String bitString) {
        Map<Integer, String> map = new HashMap<>();
        String[] split = bitString.split("-");
        for (String s : split) {
            String[] huffys = s.split(":");
            map.put(Integer.parseInt(huffys[0]), huffys[1]);
        }
        HuffmanTree tree = new HuffmanTree();
        for (Integer key : map.keySet()) {
            tree.root = createTree(key, map.get(key), tree.root);
        }
        return tree;
    }

    private HuffmanNode createTree(int key, String bitString, HuffmanNode node) {
        if (node == null) {
            node = new HuffmanNode(0, 0);
        }

        if (!bitString.isEmpty() && (int) bitString.charAt(0) == '0') {
            if (node.left == null) {
                node.left = new HuffmanNode(0, 0);
            }
            node.left = createTree(key, bitString.substring(1), node.left);


        }
        if (!bitString.isEmpty() && (int) bitString.charAt(0) == '1') {
            if (node.left == null) {
                node.left = new HuffmanNode(0, 0);
            }
            node.right = createTree(key, bitString.substring(1), node.right);

        }
        if (bitString.isEmpty()) {
            node.character = key;
        }
        return node;
    }


    // Decodieren eines bitStrings anhand der in tree gespeicherten Kodierung
    public String readWithTree(HuffmanTree tree, String bits) {
        Map<Integer, String> map = new LinkedHashMap<>();

        StringBuilder sb = new StringBuilder();
        while (!bits.isEmpty()) {
            map = readChar(bits, tree.root);
            int c = map.entrySet().iterator().next().getKey();
            bits = map.get(c);
            sb.append((char) c);
        }

        return sb.toString();
    }

    private Map<Integer, String> readChar(String bits, HuffmanNode node) {
        if (node.character != 0) {
            Map<Integer, String> map = new LinkedHashMap<>();
            map.put(node.character, bits);
            return map;
        }
        if (bits.charAt(0) == '0') {
            return readChar(bits.substring(1), node.left);
        } else {
            return readChar(bits.substring(1), node.right);
        }
    }


    // Node in Huffmanbaum
    class HuffmanNode {
        int character;
        int frequency;
        HuffmanNode left, right;

        public HuffmanNode(int character, int frequency) {
            this.character = character;
            this.frequency = frequency;
            this.left = null;
            this.right = null;
        }
    }

    //Komperator für die Vergleiche der Vorkommnisse eines buchstabens
    class NodeComparator implements Comparator<HuffmanNode> {
        public int compare(HuffmanNode node1, HuffmanNode node2) {
            return node1.frequency - node2.frequency;
        }
    }
}
