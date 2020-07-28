package shop.ui;

public interface UI {
  public void processMenu(MenuElement menu);
  public String[] processForm(FormElement form);
  public void displayMessage(String message);
  public void displayError(String message);
}
