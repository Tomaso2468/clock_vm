package vm;

public abstract class Device extends Thread {
	public void startDevice() {
		System.out.println("Starting " + this);
		start();
	}
	@SuppressWarnings("deprecation")
	public void stopDevice() {
		System.out.println("Stopping " + this);
		stop();
	}
	public abstract void run();
	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode());
	}
}
