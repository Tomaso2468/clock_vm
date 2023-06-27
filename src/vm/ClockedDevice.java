package vm;

public abstract class ClockedDevice extends Device {
	private final Clock clock;
	public ClockedDevice(Clock clock) {
		this.clock = clock;
	}
	@Override
	public void run() {
		init();
		clock.waitUntilNext();
		while(true) {
			doAction();
			clock.waitUntilNext();
		}
	}
	public void init() {
	}
	public abstract void doAction();
	public Clock getClock() {
		return clock;
	}
}
