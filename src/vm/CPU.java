package vm;

public class CPU extends ClockedDevice {
	private Cache cache;
	public CPU() {
		super(new ControlUnit(1));
	}
	
	@Override
	public void init() {
		cache = new Cache(getClock(), 512);
		cache.start();
	}

	@Override
	public void doAction() {
		byte a = cache.get(0);
		System.out.println(a);
		System.exit(0);
	}

}
