package vm;

import java.util.List;
import java.util.Vector;

public class Memory extends ClockedDevice {
	private byte[] data;
	private List<MemoryRequest> requests = new Vector<MemoryRequest>();
	public Memory(double speed, long size) {
		super(new MemoryClock(speed));
		data = new byte[(int) size];
	}
	
	@Override
	public void init() {
		((MemoryClock) getClock()).startDevice();
	}
	
	private static class MemoryRequest {
		private long address;
		private Cache cache;
		public MemoryRequest(long address, Cache cache) {
			super();
			this.address = address;
			this.cache = cache;
		}
	}

	private static class MemoryClock extends Device implements Clock {
		private long tick = 0;
		private double speed = 0;
		public MemoryClock(double speed) {
			this.speed = speed;
		}
		@Override
		public long getTime() {
			return tick;
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
					Thread.sleep((long) (1000 / speed));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				tick += 1;
			}
		}
		
	}
	
	public void request(long address, Cache cache) {
		requests.add(new MemoryRequest(address, cache));
	}

	@Override
	public void doAction() {
		if(requests.size() > 0) {
			MemoryRequest request = requests.get(0);
			requests.remove(0);
			request.cache.receive(data[(int) request.address]);
		}
	}
}
