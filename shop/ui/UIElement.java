package shop.ui;

import java.util.List;

abstract class UIElement<T> implements Element {
	final String _heading;
	final List<UIPair<String,T>> _element;
	
	UIElement(String heading, List<UIPair<String,T>> element){
		_heading = heading;
		_element = element;
	}
	public int size() {
		return _element.size();
	}
	public String getHeading() {
		return _heading;
	}
	public String getPrompt(int i) {
		return _element.get(i).first();
	}
}

class NewUIForm extends UIElement<UIFormTest> implements FormElement {
	NewUIForm(String heading, List<UIPair<String,UIFormTest>> element){
		super(heading, element);
	}
	public boolean checkInput(int i, String input) {
		if (null == _element.get(i))
			return true;
		return _element.get(i).second().run(input);
	}
}

class NewUIMenu extends UIElement<UIMenuAction> implements MenuElement {
	NewUIMenu(String heading, List<UIPair<String,UIMenuAction>> element){
		super(heading, element);
	}
	public void runAction(int i) {
		_element.get(i).second().run();
	}
}
