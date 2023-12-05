import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

        createLookuptable();

        // Huffmantree aus CodierungsTabelle erzeugen
        HuffmanTree ht = new HuffmanTree();
        ht = ht.readCodingTable(getFile("dec_tab-mada.txt"));

        //output-mada.dat einlesen
        File file = new File("output-mada.dat");
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
        binaryString = binaryString.substring(0,binaryString.lastIndexOf('1'));
        // Text decodieren und in decompress.txt speichern
        writeFile("decompress.txt",ht.readWithTree(ht,binaryString));
    }

    private static void createLookuptable() {
        String text = getFile("text.txt");
        int[] lookupTable = new int[128];

        assert text != null;
        for (char c: text.toCharArray()) {
            lookupTable[c]++;
        }
        HuffmanTree hf = new HuffmanTree();


    }

    public static void tableToFile(int[] table){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < table.length; i++) {
            if (table[i] > 0) {
                sb.append(i);
                sb.append(":");
                sb.append(table[i]);
                if (i < table.length -1) {
                    sb.append("-");
                }
            }
        }

        writeFile("dec_tab.txt", sb.toString());
    }

    public static void writeFile(String Filename, String content){
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
