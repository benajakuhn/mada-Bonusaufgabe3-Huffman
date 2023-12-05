import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        //lookuptable für text.txt
        HuffmanTree tree = createLookuptable("text.txt");

        // encode text.txt
        encodeFile(tree, "text.txt", "output.dat");

        //decode output.dat
        decodeFile(tree, "output.dat", "decompress_ours.txt");

        //lookuptable für text.txt
        HuffmanTree treeAndy = createLookuptable("andys-story.txt");

        // encode text.txt
        encodeFile(treeAndy, "andys-story.txt", "output_andy.dat");

        //decode output.dat
        decodeFile(treeAndy, "output_andy.dat", "decompress_andy.txt");


        // Huffmantree aus CodierungsTabelle erzeugen
        HuffmanTree ht = new HuffmanTree();
        ht = ht.readCodingTable(getFile("dec_tab-mada.txt"));

        //output-mada.dat decodieren
        decodeFile(ht, "output-mada.dat", "decompress.txt");
    }

    private static void decodeFile(HuffmanTree ht, String fileIn, String fileOut) throws IOException {
        File file = new File(fileIn);
        byte[] bFile = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bFile);
        fis.close();
        StringBuilder sb = new StringBuilder();
        for (byte b : bFile) {
            String s2 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            sb.append(s2);
        }
        String binaryString = sb.toString();
        binaryString = binaryString.substring(0, binaryString.lastIndexOf('1'));
        writeFile(fileOut, ht.readWithTree(ht, binaryString));
    }

    private static void encodeFile(HuffmanTree tree, String fileIn, String fileOut) throws IOException {
        char[] textChars = getFile(fileIn).toCharArray();
        int[] textInt = new int[textChars.length];
        for (int i = 0; i < textChars.length; i++) {
            textInt[i] = textChars[i];
        }
        StringBuilder encoded = new StringBuilder(tree.encode(tree, textInt));
        int add = 8 - (encoded.length() % 8);
        if (add != 8) {
            encoded.append("1");
            add--;
            encoded.append("0".repeat(add));
        }

        byte[] array = binaryStringToByteArray(encoded.toString());

        FileOutputStream fos = new FileOutputStream(fileOut);
        fos.write(array);
        fos.close();
    }

    private static byte[] binaryStringToByteArray(String binaryString) {
        int length = binaryString.length();
        byte[] byteArray = new byte[length / 8];
        for (int i = 0; i < length; i += 8) {
            String byteString = binaryString.substring(i, Math.min(i + 8, length));
            byte b = (byte) Integer.parseInt(byteString, 2);
            byteArray[i / 8] = b;
        }
        return byteArray;
    }

    private static HuffmanTree createLookuptable(String file) {
        String text = getFile(file);
        int[] lookupTable = new int[128];

        assert text != null;
        for (char c : text.toCharArray()) {
            lookupTable[c]++;
        }
        ArrayList<Integer> chars = new ArrayList<>();
        ArrayList<Integer> frequencies = new ArrayList<>();
        for (int i = 0; i < lookupTable.length; i++) {
            if (lookupTable[i] > 0) {
                frequencies.add(lookupTable[i]);
                chars.add(i);
            }
        }

        HuffmanTree tree =
            new HuffmanTree(chars.stream().mapToInt(i -> i).toArray(), frequencies.stream().mapToInt(i -> i).toArray());
        writeFile("dec_tab.txt", tree.generateCodingTable(tree, chars.stream().mapToInt(i -> i).toArray()));
        return tree;
    }

    public static void writeFile(String Filename, String content) {
        try {
            BufferedWriter fr = new BufferedWriter(new FileWriter(Filename));
            fr.write(content);
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFile(String fileName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            br.close();

            return String.valueOf(content);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
