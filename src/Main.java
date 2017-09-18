import model.TapeFormatMatrix;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class Main {
    private static final String AL_PATH = System.getProperty("user.dir") + "/src/assets/al.txt";
    private static final String AU_PATH = System.getProperty("user.dir") +  "/src/assets/au.txt";
    private static final String DI_PATH = System.getProperty("user.dir") + "/src/assets/di.txt";
    private static final String B_PATH = System.getProperty("user.dir") + "/src/assets/b.txt";
    final static Charset ENCODING = StandardCharsets.UTF_8;

    public static final int DIAMETR = 3;
    public static final int MATRIX_SIZE = 6;

    private int matrixSize;
    private static Float[][] al;
    private static Float[][] au;
    private static Float[] di;
    private static Float[] b;

    public static void main(String[] args) {
        // Initialization
        al = new Float[MATRIX_SIZE][DIAMETR];
        au = new Float[MATRIX_SIZE][DIAMETR];
        di = new Float[MATRIX_SIZE];
        b = new Float[MATRIX_SIZE];
        // Reading matrix
        readAl();
        readAU();
        readDI();
        readB();
        TapeFormatMatrix matrix = new TapeFormatMatrix(MATRIX_SIZE, al, au, di, DIAMETR);
        Float[] x = matrix.calculateAxEqualsB(b);
        int length = x.length;
    }

    private static void readAl(){
        al = readLargerTextFileDouble(AL_PATH);
    }
    private static void readAU(){
        au = readLargerTextFileDouble(AU_PATH);
    }
    private static void readDI(){
        di = readLargerTextFile(DI_PATH);
    }
    private static void readB(){
        b = readLargerTextFile(B_PATH);
    }

    private static void handleTextLine(String line, ArrayList<Float> al){
        String[] split = line.split(" ");
        for(String s: split){
            al.add(Float.parseFloat(s));
        }
    }

    private static Float[][] readLargerTextFileDouble(String aFileName){
        Path path = Paths.get(aFileName);
        ArrayList<Float> al = new ArrayList<>();
        try (Scanner scanner =  new Scanner(path, ENCODING.name())){
            while (scanner.hasNextLine()){
                //process each line in some way
                handleTextLine(scanner.nextLine(), al);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        Float[][] floats = new Float[MATRIX_SIZE][DIAMETR];
        Iterator<Float> iterator = al.iterator();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < DIAMETR; j++) {
                floats[i][j] = iterator.next();
            }
        }
        return floats;
    }

    private static Float[] readLargerTextFile(String aFileName){
        Path path = Paths.get(aFileName);
        ArrayList<Float> al = new ArrayList<>();
        try (Scanner scanner =  new Scanner(path, ENCODING.name())){
            while (scanner.hasNextLine()){
                //process each line in some way
                handleTextLine(scanner.nextLine(), al);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        Float[] floats = new Float[MATRIX_SIZE];
        Iterator<Float> iterator = al.iterator();
        for (int i = 0; i < MATRIX_SIZE; i++) {
            floats[i] = iterator.next();
        }
        return floats;
    }
}
