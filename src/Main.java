import java.io.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Andy Spoonys little world");
        createLookuptable();
    }

    private static void createLookuptable() {
        String text = getFile("text.txt");
        int[] lookupTable = new int[128];

        assert text != null;
        for (char c: text.toCharArray()) {
            lookupTable[c]++;
        }

        tableToFile(lookupTable);
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
    public static void codeBitstring(){
       String content = getFile("dec_tab.txt");

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
