package shop.command;

public class CommandHistoryFactory {
  private CommandHistoryFactory() {}
  static public CommandHistory newCommandHistory() {
	CommandHistory cmdHst = new CommandHistoryObj();
    return cmdHst;
  }
}
