package shop.ui;

public interface FormBuilder extends ElementBuilder<UIFormTest> {
	//public void add(String prompt, UIFormTest item);
	public FormElement toUIElement(String heading);
}
