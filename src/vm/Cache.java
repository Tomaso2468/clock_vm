package vm;

import java.util.ArrayList;
import java.util.List;

public class Cache extends ClockedDevice {
	private long address;
	private boolean done;
	private byte data;
	private boolean request;
	private int size;
	private long lastAddress;
	private byte mem;
	private boolean mem_done;
	private List<CacheEntry> entries;
	public Cache(Clock clock, int size) {
		super(clock);
		this.size = size;
		entries = new ArrayList<CacheEntry>(size + 1);
	}
	private class CacheEntry {
		private final long address;
		private final byte data;
		private final long age;
		public CacheEntry(long address, byte data) {
			this.address = address;
			this.data = data;
			this.age = getClock().getTime();
		}
		public long getAddress() {
			return address;
		}
		public byte getData() {
			return data;
		}
		public boolean isOld() {
			return getClock().getTime() - age > 256;
		}
	}
	public void checkForOldRecords() {
		for(CacheEntry e : entries) {
			if(e.isOld()) {
				entries.remove(e);
			}
		}
	}
	public void attemptAdd(CacheEntry e) {
		if(entries.size() + 1 > size) {
			entries.remove(0);
		}
		entries.add(e);
	}
	public byte cache(long address) {
		if(getFromCache(address) != null) {
			return getFromCache(address).data;
		}
		Board.memory.request(address, this);
		while(!mem_done) {
			getClock().waitUntilNext();
		}
		attemptAdd(new CacheEntry(address, mem));
		mem_done = false;
		return mem;
	}
	public void cacheData(long address, long length) {
		for(long i = address; i < address + length; i++) {
			cache(i);
		}
	}
	public void cacheData(long address) {
		cacheData(address, 128);
	}
	public CacheEntry getFromCache(long address) {
		for(CacheEntry e : entries) {
			if(e.getAddress() == address) {
				return e;
			}
		}
		return null;
	}
	public void checkForRequest() {
		if(request) {
			if (address - lastAddress < 8 && address - lastAddress > 0) {
				cacheData(address);
			}
			if (address == lastAddress) {
				cache(address);
			}
			CacheEntry e = getFromCache(address);
			if(e != null) {
				request = false;
				data = e.getData();
				done = true;
			} else {
				data = cache(address);
				request = false;
				done = true;
			}
		}
	}
	@Override
	public void doAction() {
		checkForOldRecords();
		checkForRequest();
	}
	protected void request(long address) {
		request = true;
		lastAddress = this.address;
		this.address = address;
		done = false;
	}
	public byte get(long address) {
		request(address);
		while(!done) {
			getClock().waitUntilNext();
		}
		return data;
	}
	public void receive(byte data) {
		mem = data;
		mem_done = true;
	}
}
