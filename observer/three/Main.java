package observer.three;
public class Main {
  public static void main(String[] argv) {
    Int c = new Int();
    Runnable r1 = new M(c);
    Runnable r2 = new N(c);
    for (int i=0; i<10000; i++) {
      r1.run();
      r2.run();
    }
  }
}
class Int {
  private int _v;
  private int _numOps;
  public void inc() { _v++; System.out.println (++_numOps); }
  public void dec() { _v--; System.out.println (++_numOps); }
}
class M implements Runnable {
  private Int _c;
  public M(Int c) { _c = c; }
  public void run() {
    _c.inc();
    _c.inc();
    _c.dec();
  }
}
class N implements Runnable {
  private Int _c;
  public N(Int c) { _c = c; }
  public void run() {
    for (int i=0; i<50; i++) {
      if (i%3==0) {
        _c.dec();
      } else {
        _c.inc();
      }
    }
  }
}
