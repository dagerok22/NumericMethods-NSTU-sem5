package model;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class TapeFormatMatrix {
    private int matrixSize;
    private ArrayList<Float> al;
    private ArrayList<Float> au;
    private ArrayList<Float> di;
    private int diameter;

    public TapeFormatMatrix(int numberOfElements,
                            ArrayList<Float> al,
                            ArrayList<Float> au,
                            ArrayList<Float> di,
                            int diameter) {
        matrixSize = numberOfElements;
        this.al = al;
        this.au = au;
        this.di = di;
        this.diameter = diameter;
    }

    public ArrayList<Float> multiplyByVector(List<Float> vector) {
        ArrayList<Float> resultVector = new ArrayList<>();
        for (int i = 0; i < matrixSize; i++) {
            resultVector.add(vector.get(i) * di.get(i));
        }
        for (int i = 0; i < matrixSize; i++) {
            Float iResultElement = resultVector.get(i);
            int firstElInRow = getIndexOfFirstElInRow(i);
            for (int j = 0; j < min(diameter, i); j++) {
                int offset = i - diameter > 0 ? i - diameter : 0;
                iResultElement += al.get(firstElInRow + j) * vector.get(offset+j);
            }
            for (int j = 0; j < min(diameter, matrixSize - i - 1); j++) {
                Float auEl = au.get(diameter * i + j);
                Float vectorEl = vector.get(i + j+1);
                iResultElement += auEl * vectorEl;
            }
            resultVector.set(i, iResultElement);
        }
        return resultVector;

    }

    private int getIndexOfFirstElInRow(int i) {
        if (diameter - i > 0) {
            return diameter * i + diameter - i;
        }
        return diameter * i;
    }
}
