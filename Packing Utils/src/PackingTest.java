
import acm.program.ConsoleProgram;
import acm.util.RandomGenerator;

public class PackingTest extends ConsoleProgram {

    private static final RandomGenerator RGEN = RandomGenerator.getInstance();
    private static final int MAXTEST = 100;
    private static final int LENGTH = 256;
    private byte[] buffer = new byte[LENGTH];

    public void run() {
        testBooleans();
        testChars();
        testStrings();
        testInts();
        testLongs();
        testFloats();
        testDoubles();
    }

    private void testBooleans() {
        println("Begin boolean test");
        PackUtils.packBoolean(false, buffer, 0);
        PackUtils.packBoolean(true, buffer, 1);
        if ( PackUtils.unpackBoolean(buffer, 0) ) {
            println("Error recovering false");
        }
        if ( ! PackUtils.unpackBoolean(buffer, 1) ) {
            println("Error recovering true");
        }
        println("Begin end test");
    }
    
    private void testChars() {
        println("Begin char test");
        for (int i = 0; i < MAXTEST; i++) {
            char in = (char) RGEN.nextInt('A', 'Z');
            PackUtils.packChar(in, buffer, 0);
            char out = PackUtils.unpackChar(buffer, 0);
            if (in != out) {
                println("Error: " + in + " != " + out);
            }
        }
        println("End char test");
    }

    private void testStrings() {
        println("Begin string test");
        for (int i = 0; i < MAXTEST; i++) {
            String in = "";
            int maxLength = RGEN.nextInt(LENGTH/2);
            for (int j = 0; j < RGEN.nextInt(LENGTH); j++) {
                in += (char) RGEN.nextInt('A', 'Z');
            }
            PackUtils.packLimitedString(in, maxLength, buffer, 0);
            String out = PackUtils.unpackLimitedString(maxLength, buffer, 0);
            if ( ! in.substring(0, Math.min(maxLength, in.length())).equals(out) ) {
                println("Error: " + in + " != " + out);
            }
        }
        println("End string test");
    }

    private void testInts() {
        println("Begin int test");
        for (int i = 0; i < MAXTEST; i++) {
            int in = RGEN.nextInt();
            PackUtils.packInt(in, buffer, 0);
            int out = PackUtils.unpackInt(buffer, 0);
            if (in != out) {
                println("Error: " + in + " != " + out);
            }
        }
        println("End int test");
    }

    private void testLongs() {
        println("Begin long test");
        for (int i = 0; i < MAXTEST; i++) {
            long in = RGEN.nextLong();
            PackUtils.packLong(in, buffer, 0);
            long out = PackUtils.unpackLong(buffer, 0);
            if (in != out) {
                println("Error: " + in + " != " + out);
            }
        }
        println("End int test");
    }
    
    private void testFloats() {
        println("Begin float test");
        for (int i = 0; i < MAXTEST; i++) {
            float in = RGEN.nextFloat();
            PackUtils.packFloat(in, buffer, 0);
            float out = PackUtils.unpackFloat(buffer, 0);
            if (in != out) {
                println("Error: " + in + " != " + out);
            }
        }
        println("End float test");
    }
    
    private void testDoubles() {
        println("Begin double test");
        for (int i = 0; i < MAXTEST; i++) {
            double in = RGEN.nextDouble();
            PackUtils.packDouble(in, buffer, 0);
            double out = PackUtils.unpackDouble(buffer, 0);
            if (in != out) {
                println("Error: " + in + " != " + out);
            }
        }
        println("End double test");
    }
}
