package jvm0105;

public class LocalVariableTest {
  public static void main(String[] args) {
    MovingAverage ma = new MovingAverage();
    int num1 = 1;
    int num2 = 6;
    ma.submit(num1);
    ma.submit(num2);
    double avg = ma.getAvg();
  }
}
