package shop.ui;

final class UIPair<S,T> {
	  final private S _x;
	  final private T _y;
	  UIPair(S x, T y) {
	    if (x == null || y == null)
	      throw new IllegalArgumentException();
	    _x = x;
	    _y = y;
	  }
	  S first() { return _x; }
	  T second() { return _y; }
}
