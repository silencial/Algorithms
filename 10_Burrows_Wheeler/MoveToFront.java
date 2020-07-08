import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // Apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] ascii = buildArray();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char prev = ascii[0];
            char temp, i;
            for (i = 0; ascii[i] != c; i++) {
                temp = ascii[i];
                ascii[i] = prev;
                prev = temp;
            }
            ascii[i] = prev;
            ascii[0] = c;
            BinaryStdOut.write(i);
        }
        BinaryStdOut.close();
    }

    // Apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] ascii = buildArray();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char out = ascii[c];
            BinaryStdOut.write(out);
            while (c > 0)
                ascii[c] = ascii[--c];
            ascii[0] = out;
        }
        BinaryStdOut.close();
    }

    private static char[] buildArray() {
        char[] chars = new char[R];
        for (char i = 0; i < R; i++)
            chars[i] = i;
        return chars;
    }

    // If args[0] is "-", apply move-to-front encoding
    // If args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException();
    }
}
