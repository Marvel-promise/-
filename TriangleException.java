package Geometric.My_Exception;
public class TriangleException extends Exception{
    private int a,b,c;
    public TriangleException(int a,int b,int c){
        super("这三边不能组成三角形" + a + b + c);
        this.a=a;this.b = b;this.c = c;
    }
    public int getA() {
        return a;
    }
    public int getB() {
        return b;
    }
    public int getC() {
        return c;
    }
}
