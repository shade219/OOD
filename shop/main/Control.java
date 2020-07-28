package shop.main;

import shop.ui.UI;
import shop.ui.UIMenuAction;
import shop.ui.UIError;
import shop.ui.UIFormTest;
import shop.ui.UIFactory;
import shop.data.Data;
import shop.data.Inventory;
import shop.data.Video;
import shop.data.Record;
import shop.command.Command;
import java.util.Iterator;
import shop.ui.FormElement;
import shop.ui.MenuElement;
import shop.ui.FormBuilder;
import shop.ui.MenuBuilder;

import junit.framework.Assert;

import java.util.Comparator;

class Control {

  private FormElement _getVideoForm;
  private UIFormTest _numberTest;
  private UIFormTest _stringTest;
    
  private Inventory _inventory;
  private UI _ui;
  
  State _curState = new StateStart();
  
  interface State {
	  public void run();
  }
  
  class StateStart implements State{
	  public void run() {
		  try {
			  _ui.processMenu(MenuStates.START.getME());
		  }catch(UIError e) {
			  _ui.displayError("UI closed");
		  }
		  _curState.run();
	  }
  }
  class StateExit implements State{
	  public void run() {
		  try {
			  _ui.processMenu(MenuStates.EXIT.getME());
		  }catch(UIError e) {
			  _ui.displayError("UI closed");
		  }
		  _curState.run();
	  }
  }
  class StateExited implements State{
	  public void run() {}
  }
  
  private enum MenuStates {
	  START(null),
	  EXIT(null),
	  EXITED(null);
	  
	  private MenuElement me;
	  
	  MenuStates(MenuElement me){this.me = me;}
	  
	  MenuElement getME() {
		  return this.me;
	  }
	  
	  void setME(MenuElement me) {
		  this.me = me;
	  }
  }
  
  Control(Inventory inventory, UI ui) {
    _inventory = inventory;
    _ui = ui;

    //_menus = new Menu[NUMSTATES];
    MenuStates.START.setME(addSTART());
    MenuStates.EXIT.setME(addEXIT());
    
    
    UIFormTest yearTest = new UIFormTest() {
        public boolean run(String input) {
          try {
            int i = Integer.parseInt(input);
            return i > 1800 && i < 5000;
          } catch (NumberFormatException e) {
            return false;
          }
        }
      };
    _numberTest = new UIFormTest() {
        public boolean run(String input) {
          try {
            Integer.parseInt(input);
            return true;
          } catch (NumberFormatException e) {
            return false;
          }
        }
      };
    _stringTest = new UIFormTest() {
        public boolean run(String input) {
          return ! "".equals(input.trim());
        }
      };

    FormBuilder f = UIFactory.newEFormBuilder();
    //FormBuilder f = UIFactory.newFormBuilder();
    f.add("Title", _stringTest);
    f.add("Year", yearTest);
    f.add("Director", _stringTest);
    //_getVideoForm = f.toUIForm("Enter Video");
    _getVideoForm = f.toUIElement("Enter Video");
  }
  
  void run() {
    _curState.run();
  }
  
  private MenuElement addSTART() {
    MenuBuilder m = UIFactory.newEMenuBuilder();
    
    m.add("Default",
      new UIMenuAction() {
        public void run() {
          _ui.displayError("doh!");
        }
      });
    m.add("Add/Remove copies of a video",
      new UIMenuAction() {
        public void run() {
          String[] result1 = _ui.processForm(_getVideoForm);
          Video v = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);

          FormBuilder f = UIFactory.newEFormBuilder();
          f.add("Number of copies to add/remove", _numberTest);
          String[] result2 = _ui.processForm(f.toUIElement(""));
                                             
          Command c = Data.newAddCmd(_inventory, v, Integer.parseInt(result2[0]));
          if (! c.run()) {
            _ui.displayError("Command failed");
          }
        }
      });
    m.add("Check in a video",
      new UIMenuAction() {
        public void run() {
          // TODO
            String[] result1 = _ui.processForm(_getVideoForm);
            Video v = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);
            
            Command c = Data.newInCmd(_inventory, v);
            if (! c.run()) {
            	_ui.displayError("command failed");
            }
        }
      });
    m.add("Check out a video",
      new UIMenuAction() {
        public void run() {
          // TODO
            String[] result1 = _ui.processForm(_getVideoForm);
            Video v = Data.newVideo(result1[0], Integer.parseInt(result1[1]), result1[2]);
            
            Command c = Data.newOutCmd(_inventory, v);
            if (! c.run()) {
            	_ui.displayError("command failed");
            }
        }
      });
    m.add("Print the inventory",
      new UIMenuAction() {
        public void run() {
          _ui.displayMessage(_inventory.toString());
        }
      });
    m.add("Clear the inventory",
      new UIMenuAction() {
        public void run() {
          if (!Data.newClearCmd(_inventory).run()) {
            _ui.displayError("Command failed");
          }
        }
      });
    m.add("Undo",
      new UIMenuAction() {
        public void run() {
          if (!Data.newUndoCmd(_inventory).run()) {
            _ui.displayError("Command failed");
          }
        }
      });
    m.add("Redo",
      new UIMenuAction() {
        public void run() {
          if (!Data.newRedoCmd(_inventory).run()) {
            _ui.displayError("Command failed");
          }
        }
      });
    m.add("Print top ten all time rentals in order",
      new UIMenuAction() {
        public void run() {
          // TODO
        	StringBuilder b = new StringBuilder();
            Comparator<Record> c = new Comparator<Record>() {
                public int compare(Record r1, Record r2) {
                  return r2.numRentals() - r1.numRentals();
                }
              };
              Iterator<Record> i = _inventory.iterator(c);
              int tenCount = 0;
              while (tenCount < 10) {
                  b.append(i.next().toString());
                  b.append("\n");
                  tenCount++;
                }
              _ui.displayMessage(b.toString());
        }
      });
          
    m.add("Exit",
      new UIMenuAction() {
        public void run() {
          _curState = new StateExit();
        }
      });
    
    m.add("Initialize with bogus contents",
      new UIMenuAction() {
        public void run() {
          Data.newAddCmd(_inventory, Data.newVideo("a", 2000, "m"), 1).run();
          Data.newAddCmd(_inventory, Data.newVideo("b", 2000, "m"), 2).run();
          Data.newAddCmd(_inventory, Data.newVideo("c", 2000, "m"), 3).run();
          Data.newAddCmd(_inventory, Data.newVideo("d", 2000, "m"), 4).run();
          Data.newAddCmd(_inventory, Data.newVideo("e", 2000, "m"), 5).run();
          Data.newAddCmd(_inventory, Data.newVideo("f", 2000, "m"), 6).run();
          Data.newAddCmd(_inventory, Data.newVideo("g", 2000, "m"), 7).run();
          Data.newAddCmd(_inventory, Data.newVideo("h", 2000, "m"), 8).run();
          Data.newAddCmd(_inventory, Data.newVideo("i", 2000, "m"), 9).run();
          Data.newAddCmd(_inventory, Data.newVideo("j", 2000, "m"), 10).run();
          Data.newAddCmd(_inventory, Data.newVideo("k", 2000, "m"), 11).run();
          Data.newAddCmd(_inventory, Data.newVideo("l", 2000, "m"), 12).run();
          Data.newAddCmd(_inventory, Data.newVideo("m", 2000, "m"), 13).run();
          Data.newAddCmd(_inventory, Data.newVideo("n", 2000, "m"), 14).run();
          Data.newAddCmd(_inventory, Data.newVideo("o", 2000, "m"), 15).run();
          Data.newAddCmd(_inventory, Data.newVideo("p", 2000, "m"), 16).run();
          Data.newAddCmd(_inventory, Data.newVideo("q", 2000, "m"), 17).run();
          Data.newAddCmd(_inventory, Data.newVideo("r", 2000, "m"), 18).run();
          Data.newAddCmd(_inventory, Data.newVideo("s", 2000, "m"), 19).run();
          Data.newAddCmd(_inventory, Data.newVideo("t", 2000, "m"), 20).run();
          Data.newAddCmd(_inventory, Data.newVideo("u", 2000, "m"), 21).run();
          Data.newAddCmd(_inventory, Data.newVideo("v", 2000, "m"), 22).run();
          Data.newAddCmd(_inventory, Data.newVideo("w", 2000, "m"), 23).run();
          Data.newAddCmd(_inventory, Data.newVideo("x", 2000, "m"), 24).run();
          Data.newAddCmd(_inventory, Data.newVideo("y", 2000, "m"), 25).run();
          Data.newAddCmd(_inventory, Data.newVideo("z", 2000, "m"), 26).run();
        }
      });
    
    return m.toUIElement("Bob's Video");
  }
  private MenuElement addEXIT() {
    MenuBuilder m = UIFactory.newEMenuBuilder();
    
    m.add("Default", new UIMenuAction() { public void run() {} });
    m.add("Yes",
      new UIMenuAction() {
        public void run() {
          _curState = new StateExited();
        }
      });
    m.add("No",
      new UIMenuAction() {
        public void run() {
          _curState = new StateStart();
        }
      });
    
    return m.toUIElement("Are you sure you want to exit?");
  }
}
