package shop.ui;

public class UIFactory {
  private UIFactory() {}
  //static private UI _UI = new PopupUI();
  //static private UI _UI = new TextUI();
  static public UI newTextUI () {
    return new TextUI();
  }
  static public UI newPopupUI () {
	  return new PopupUI();
  }
  static public FormBuilder newEFormBuilder () {
	  return new FormElementBuilder();
  }
  static public MenuBuilder newEMenuBuilder () {
	  return new MenuElementBuilder();
  }
}
