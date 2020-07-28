package shop.ui;

import javax.swing.JOptionPane;
//import java.io.IOException;

final class PopupUI implements UI {
  public void displayMessage(String message) {
    JOptionPane.showMessageDialog(null,message);
  }

  public void displayError(String message) {
    JOptionPane.showMessageDialog(null,message,"Error",JOptionPane.ERROR_MESSAGE);
  }

  public void processMenu(MenuElement menu) {
    StringBuilder b = new StringBuilder();
    b.append(menu.getHeading());
    b.append("\n");
    b.append("Enter choice by number:");
    b.append("\n");

    for (int i = 1; i < menu.size(); i++) {
      b.append("  " + i + ". " + menu.getPrompt(i));
      b.append("\n");
    }

    String response = JOptionPane.showInputDialog(b.toString());
    if (response == null) {
      response = "";
    }
    int selection;
    try {
      selection = Integer.parseInt(response, 10);
      if ((selection < 0) || (selection >= menu.size()))
        selection = 0;
    } catch (NumberFormatException e) {
      selection = 0;
    }

    menu.runAction(selection);
  }

  public String[] processForm(FormElement form) {
    // TODO
		int currentElement = 0;
		String[] formResult = new String[form.size()];

		while (currentElement < form.size()) {
		    StringBuilder b = new StringBuilder();
		    b.append(form.getHeading());
		    b.append("\n");
			b.append("Please Input Information for the following prompt:");
			b.append("\n");
			b.append(form.getPrompt(currentElement));
			b.append("\n");
			
			String response = JOptionPane.showInputDialog(b.toString());
			boolean testResult = form.checkInput(currentElement, response);
			if(testResult) {
				formResult[currentElement] = response;
				currentElement++;
			}
			else {
				StringBuilder b2 = new StringBuilder();
				b2.append("Invalid Response. Please Try Again");
				JOptionPane.showMessageDialog(null, b2);
			}
		}	
	    return formResult;
  }
}
