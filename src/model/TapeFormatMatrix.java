package model;


public class TapeFormatMatrix {
    private int matrixSize;
    private Float[][] al;
    private Float[][] au;
    private Float[] di;
    private Float[] y;
    private Float[] b;
    private int diameter;

    public TapeFormatMatrix(int numberOfElements,
                            Float[][] al,
                            Float[][] au,
                            Float[] di,
                            int diameter) {
        matrixSize = numberOfElements;
        this.al = al;
        this.au = au;
        this.di = di;
        this.diameter = diameter;
    }


    public void setB(Float[] b) {
        this.b = b;
    }

    public Float[] calculateXVector() {
        int D1 = diameter - 1;
        for (int i = matrixSize - 2, i2 = 0; i2 < D1; i--, i2++)
            for (int k = i + 1, j = diameter - 1, i3 = 0; i3 <= i2; k++, j--, i3++)
                b[i] -= au[k][j] * b[k];

        for (int i = matrixSize - diameter - 1; i >= 0; i--)
            for (int k = i + 1, j = diameter - 1; j >= 0; k++, j--)
                b[i] -= au[k][j] * b[k];

        return b;
    }

    public Float[] calculateYVector() {
        this.y = this.b;
        for (int i = 1; i < diameter; i++) {
            float sum = 0f;
            int indx = getIndexOfFirstElInRow(i);
            for (int jl = indx; jl < diameter; jl++) {
                sum += y[jl - indx] * al[i][jl];
            }
            y[i] = (b[i] - sum) / di[i];
        }

        for (int i = diameter; i < matrixSize; i++) {
            float sum = 0f;
            int indx = getIndexOfFirstElInRow(i);
            for (int jl = 0, ja = i - diameter; ja < i; jl++, ja++) {
                sum += y[jl - indx] * al[i][jl];
            }
            y[i] = (b[i] - sum) / di[i];
        }
        return y;
    }

    public void calculateLU() {

        for (int i = 1; i < diameter; i++) {
            float diag = 0;
            int indexOfFirstAlBeforeDiameter = getIndexOfFirstAlBeforeDiameter(i);
            int indexOfFirstAuBeforeDiameter = getIndexOfFirstAuBeforeDiameter(i);
            for (int j = 0; j < i; j++) {
                float sumL = 0;
                float sumU = 0;
                //Fixmediag += al[i][j] * au[i][j];
                for (int k = 0; k < j; k++) {
                    sumU += au[i][indexOfFirstAuBeforeDiameter + k] * al[j][indexOfFirstAlBeforeDiameter + k];
                }
                au[i][j] = (au[i][j] - sumU) / di[i - j];
                for (int k = 0; k < j; k++) {
                    sumL += al[i][indexOfFirstAlBeforeDiameter + k] * au[j][indexOfFirstAuBeforeDiameter + k];
                }
                al[i][j] -= sumL;
                diag += al[i][indexOfFirstAlBeforeDiameter + j] * au[i][indexOfFirstAuBeforeDiameter + j];
            }
            di[i] -= diag;
        }

        for (int i = diameter; i < matrixSize; i++) {
            float diag = 0;
            for (int j = 0; j < diameter; j++) {
                float sumL = 0;
                float sumU = 0;
                int countOfZerosU = i - diameter > 0 ? i - diameter : 0;
                for (int k = 0; k < j; k++) {
                    sumU += au[i][k] * al[j + countOfZerosU][k + 1];
                }
//                au[i - (diameter - j)][j] = (au[i - (diameter - j)][j] - sumU) / di[i - (diameter - j)];
                au[i][j] = (au[i][j] - sumU) / di[i - (diameter - j)];
                for (int k = 0; k < j; k++) {
                    sumL += al[i][k] * au[i - (diameter - j)][getIndexOfAu(i, j, i - (diameter - j)) + k];
                }
                al[i][j] -= sumL;
                diag += al[i][j] * au[i][j];
            }
            di[i] -= diag;
        }
    }

    private int getIndexOfFirstElInRowLU(int i, int j) {
        int indx = diameter - (i - (diameter - j));
//        return indx > 0 ? indx : 0;
        return indx;
    }

    private int getIndexOfFirstAuBeforeDiameter(int column) {
        return diameter - column;
    }

    private int getIndexOfFirstAlBeforeDiameter(int i) {
        return diameter - i;
    }

    //Fixme не уверен на счёт -1
    private int getIndexOfAu(int i, int j, int column) {
        int zeroCountU = diameter - (i - column) - 1;
        if (zeroCountU > 0) {
            return i - diameter - zeroCountU;
        } else {
            return getIndexOfFirstElInRowLU(i, j) + i - diameter;
        }
    }


    private int getIndexOfFirstElInRow(int i) {
        return diameter - i;
    }
}
