package vm;

public interface Clock {
	public long getTime();
	public void waitUntilNext();
	public default void waitTicks(long ticks) {
		for(long i = 0; i < ticks; i++) {
			waitUntilNext();
		}
	}
}
