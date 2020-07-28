package shop.ui;

public interface MenuBuilder extends ElementBuilder<UIMenuAction> {
	//public void add(String prompt, UIMenuAction item);
	public MenuElement toUIElement(String heading);
}
