package shop.data;

/**
 * Implementation of Video interface.
 * @see Data
 */
final class VideoObj implements Video {
  private final String _title;
  private final int    _year;
  private final String _director;

  /**
   * Initialize all object attributes.
   * Title and director are "trimmed" to remove leading and final space.
   * @throws IllegalArgumentException if object invariant violated.
   */
  VideoObj(String title, int year, String director) {
    _title = title;
    _director = director;
    _year = year;
  }

  public String director() {
    // TODO
    return _director;
  }

  public String title() {
    // TODO
    return _title;
  }

  public int year() {
    // TODO
    return _year;
  }

  public boolean equals(Object thatObject) {
		if(this == thatObject) {
			return true;
		}
		if (thatObject == null) {
			return false;
		}
	    if (!(this.getClass().equals(thatObject.getClass())))
	        return false;
	      VideoObj that = (VideoObj) thatObject;
	      return _title.equals(that._title)
	          && _year == that._year
	          && _director.equals(that._director);
  }

  private int hcode;
  public int hashCode() {
	    if (hcode == 0) {
	        hcode = 17;
	        hcode = 37*hcode + _title.hashCode();
	        hcode = 37*hcode + _year;
	        hcode = 37*hcode + _director.hashCode();
	      }
	      return hcode;
  }

  public int compareTo(Video that) {
	  int iTitle = _title.compareTo(that.title());
	  if (iTitle != 0)
	    return iTitle;
	  Integer castYear = (Integer) _year; 
	  int iYear = castYear.compareTo((Integer) that.year());
	  if(iYear != 0) {
		  return iYear;
	  }
	  return _director.compareTo(that.director());
  }

  public String toString() {
    // TODO
	return _title + " (" + _year + ") : " + _director;
  }
}
