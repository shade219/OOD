package shop.ui;

import java.util.ArrayList;
import java.util.List;

abstract class UIElementBuilder<T> implements ElementBuilder<T>{
	final List<UIPair<String,T>> _element = new ArrayList<UIPair<String,T>>();
	public void add(String prompt, T item) {
		_element.add(new UIPair<String,T>(prompt,item));
	}
}

class FormElementBuilder extends UIElementBuilder<UIFormTest> implements FormBuilder{
	public FormElement toUIElement(String heading){
		return new NewUIForm(heading,_element);
	}
}
class MenuElementBuilder extends UIElementBuilder<UIMenuAction> implements MenuBuilder{
	public MenuElement toUIElement(String heading) {
		return new NewUIMenu(heading,_element);
	}
}
