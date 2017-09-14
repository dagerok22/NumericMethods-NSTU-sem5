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

    public Float[] calculateXVector(){
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

    private int getIndexOfFirstElInRow(int i) {
        return diameter - i;
    }
}
