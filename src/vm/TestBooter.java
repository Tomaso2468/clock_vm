package vm;

public class TestBooter {
	public static void main(String[] args) {
		Board.memory.startDevice();
		new CPU().startDevice();
	}
}
