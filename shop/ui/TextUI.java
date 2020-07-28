package shop.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

final class TextUI implements UI {
  final BufferedReader _in;
  final PrintStream _out;

  public TextUI() {
    _in = new BufferedReader(new InputStreamReader(System.in));
    _out = System.out;
  }

  public void displayMessage(String message) {
    _out.println(message);
  }

  public void displayError(String message) {
    _out.println(message);
  }

  private String getResponse() {
    String result;
    try {
      result = _in.readLine();
    } catch (IOException e) {
      throw new UIError(); // re-throw UIError
    }
    if (result == null) {
      throw new UIError(); // input closed
    }
    return result;
  }

  public void processMenu(MenuElement menu) {
    _out.println(menu.getHeading());
    _out.println("Enter choice by number:");

    for (int i = 1; i < menu.size(); i++) {
      _out.println("  " + i + ". " + menu.getPrompt(i));
    }

    String response = getResponse();
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
	_out.println(form.getHeading());
	while (currentElement < form.size()) {
		_out.println("Please Input Information for the following prompt:");
		_out.println(form.getPrompt(currentElement));
		
		String response = getResponse();
		boolean testResult = form.checkInput(currentElement, response);
		if(testResult) {
			formResult[currentElement] = response;
			currentElement++;
		}
		else {
			_out.println("Invalid Response. Please Try Again");
		}
	}	
    return formResult;
  }
}
