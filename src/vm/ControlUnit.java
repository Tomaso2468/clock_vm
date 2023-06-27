package vm;

public class ControlUnit extends Device implements Clock {
	private long tick = 0;
	private long speed = 0;
	public ControlUnit(long speed) {
		this.speed = speed;
	}
	@Override
	public void waitUntilNext() {
		long current = tick;
		while(current == tick) {
			Thread.yield();
		}
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(1000 / speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tick += 1;
		}
	}
	@Override
	public long getTime() {
		return tick;
	}
}
