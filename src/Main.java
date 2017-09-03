import model.TapeFormatMatrix;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    private static final String AL_PATH = System.getProperty("user.dir") + "/src/assets/al.txt";
    private static final String AU_PATH = System.getProperty("user.dir") +  "/src/assets/au.txt";
    private static final String DI_PATH = System.getProperty("user.dir") + "/src/assets/di.txt";
    final static Charset ENCODING = StandardCharsets.UTF_8;

    private int matrixSize;
    private static ArrayList<Float> al;
    private static ArrayList<Float> au;
    private static ArrayList<Float> di;

    public static void main(String[] args) {
        al = new ArrayList<>();
        au = new ArrayList<>();
        di = new ArrayList<>();
        readAl();
        readAU();
        readDI();
        Integer matrixSize = 7;
        TapeFormatMatrix matrix = new TapeFormatMatrix(matrixSize, al, au, di, 3);
        ArrayList<Float> vector = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            vector.add((float) (i + 1));
        }
        ArrayList<Float> result = matrix.multiplyByVector(vector);
        result.get(1);
    }

    private static void readAl(){
        readLargerTextFile(AL_PATH, al);
    }
    private static void readAU(){
        readLargerTextFile(AU_PATH, au);
    }
    private static void readDI(){
        readLargerTextFile(DI_PATH, di);
    }

    private static void handleTextLine(String line, ArrayList<Float> al){
        String[] split = line.split(" ");
        for(String s: split){
            al.add(Float.parseFloat(s));
        }
    }

    private static void readLargerTextFile(String aFileName, ArrayList<Float> al){
        Path path = Paths.get(aFileName);
        try (Scanner scanner =  new Scanner(path, ENCODING.name())){
            while (scanner.hasNextLine()){
                //process each line in some way
                handleTextLine(scanner.nextLine(), al);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
