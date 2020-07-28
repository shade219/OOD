package shop.data;

import java.util.Map;

import junit.framework.Assert;

import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import shop.command.Command;
import shop.command.UndoableCommand;
import shop.command.CommandHistory;
import shop.command.CommandHistoryFactory;

/**
 * Implementation of Inventory interface.
 * @see Data
 */
final class InventorySet implements Inventory {
  private Map<Video,Record> _data;
  private final CommandHistory _history;

  InventorySet() {
    // TODO
    _data = new HashMap<Video,Record>();
    _history = CommandHistoryFactory.newCommandHistory();
     
  }

  /**
   * If <code>record</code> is null, then delete record for <code>video</code>;
   * otherwise replace record for <code>video</code>.
   */
  void replaceEntry(Video video, Record record) {
    _data.remove(video);
    if (record != null)
      _data.put(video,((RecordObj)record).copy());
  }

  /**
   * Overwrite the map.
   */
  void replaceMap(Map<Video,Record> data) {
    _data = data;
  }

  public int size() {
	  return _data.size();
  }

  public Record get(Video v) {
	  return _data.get(v);
  }

  public Iterator<Record> iterator() {
    return Collections.unmodifiableCollection(_data.values()).iterator();
  }

  public Iterator<Record> iterator(Comparator<Record> comparator) {
		List<Record> l = new ArrayList<Record>(_data.values());
		Collections.sort(l, comparator);
	    return Collections.unmodifiableCollection(l).iterator();
  }

  /**
   * Add or remove copies of a video from the inventory.
   * If a video record is not already present (and change is
   * positive), a record is created. 
   * If a record is already present, <code>numOwned</code> is
   * modified using <code>change</code>.
   * If <code>change</code> brings the number of copies to be zero,
   * the record is removed from the inventory.
   * @param video the video to be added.
   * @param change the number of copies to add (or remove if negative).
   * @return A copy of the previous record for this video (if any)
   * @throws IllegalArgumentException if video null, change is zero, if attempting to remove more copies than are owned, or if attempting to remove copies that are checked out. 
   */
  Record addNumOwned(Video video, int change) {
    if (video == null || change == 0)
      throw new IllegalArgumentException();
    
    RecordObj r = (RecordObj) _data.get(video);
    if (r == null && change < 1) {
      throw new IllegalArgumentException();
    } else if (r == null) {
      _data.put(video, new RecordObj(video, change, 0, 0));
    } else if (r.numOwned+change < r.numOut) {
      throw new IllegalArgumentException();
    } else if (r.numOwned+change < 1) {
      _data.remove(video);
    } else {
      _data.put(video, new RecordObj(video, r.numOwned + change, r.numOut, r.numRentals));
    }
    return r;
  }

  /**
   * Check out a video.
   * @param video the video to be checked out.
   * @return A copy of the previous record for this video
   * @throws IllegalArgumentException if video has no record or numOut
   * equals numOwned.
   */
  Record checkOut(Video video) {
		if(video == null) {
			throw new IllegalArgumentException();
		}
		RecordObj current = (RecordObj) _data.get(video);
		if(current == null) {
			throw new IllegalArgumentException();
		}
		int owned = current.numOwned;
		int out = current.numOut;
		int curStock = owned - out;
		if(curStock <= 0) {
			throw new IllegalArgumentException();
		}
	    _data.put(video, new RecordObj(video, current.numOwned, current.numOut + 1, current.numRentals +1));
		//current.numOut += 1;
		//current.numRentals += 1;
		return current;
  }
  
  /**
   * Check in a video.
   * @param video the video to be checked in.
   * @return A copy of the previous record for this video
   * @throws IllegalArgumentException if video has no record or numOut
   * non-positive.
   */
  Record checkIn(Video video) {
		if(video == null) {
			throw new IllegalArgumentException();
		}
		RecordObj current = (RecordObj) _data.get(video);
		if(current == null) {
			throw new IllegalArgumentException();
		}
		if(current.numOut < 1) {
			throw new IllegalArgumentException();
		}
		_data.put(video, new RecordObj(video, current.numOwned, current.numOut - 1, current.numRentals));
		//current.numOut -= 1;
		return current;
  }
  
  /**
   * Remove all records from the inventory.
   * @return A copy of the previous inventory as a Map
   */
  Map<Video,Record> clear() {
	  HashMap<Video,Record> oldData = new HashMap<Video,Record>();
	  Iterator<Record> i = Collections.unmodifiableCollection(_data.values()).iterator();
	    while(i.hasNext()) {
	        Record r = i.next();
	        oldData.put(r.video(), r);
	      }
	  _data.clear();
	  return oldData;
  }

  /**
   * Return a reference to the history.
   */
  CommandHistory getHistory() {
    return _history;
  }
  
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    buffer.append("Database:\n");
    Iterator i = _data.values().iterator();
    while (i.hasNext()) {
      buffer.append("  ");
      buffer.append(i.next());
      buffer.append("\n");
    }
    return buffer.toString();
  }


  /**
   * Implementation of Record interface.
   *
   * <p>This is a utility class for Inventory.  Fields are immutable and
   * package-private.</p>
   *
   * <p><b>Class Invariant:</b> No two instances may reference the same Video.</p>
   *
   * @see Record
   */
  private static final class RecordObj implements Record {
    final Video video; // the video
    final int numOwned;   // copies owned
    final int numOut;     // copies currently rented
    final int numRentals; // total times video has been rented
    
    RecordObj(Video video, int numOwned, int numOut, int numRentals) {
      this.video = video;
      this.numOwned = numOwned;
      this.numOut = numOut;
      this.numRentals = numRentals;
    }
    RecordObj copy() {
      return new RecordObj(video, numOwned, numOut, numRentals);
    }
    public Video video() {
      return video;
    }
    public int numOwned() {
      return numOwned;
    }
    public int numOut() {
      return numOut;
    }
    public int numRentals() {
      return numRentals;
    }
    public boolean equals(Object thatObject) {
      return video.equals(((Record)thatObject).video());
    }
    public int hashCode() {
      return video.hashCode();
    }
    public String toString() {
      StringBuilder buffer = new StringBuilder();
      buffer.append(video);
      buffer.append(" [");
      buffer.append(numOwned);
      buffer.append(",");
      buffer.append(numOut);
      buffer.append(",");
      buffer.append(numRentals);
      buffer.append("]");
      return buffer.toString();
    }
  }
}
