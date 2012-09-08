package no.miles.jz2012;

import java.util.Map;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.monitor.LocalMapStats;

public class ShowNumbers {
	

	private static final String MAP_NAME = "two-backup";

	public static void main(String[] args) {
		HazelcastInstance client = null;
		try {
			ClientConfig clientConfig = new ClientConfig();
			clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
			clientConfig.addAddress("localhost");
			 client = HazelcastClient
					.newHazelcastClient(clientConfig);

			Map<Integer, FibonacciNumber> fibNumbers = client.getMap(MAP_NAME);
			System.out.println("Number of entries: " + fibNumbers.values().size());
		
//			for (FibonacciNumber number : fibNumbers.values()) {
//				System.out.println(" num:" + number.getNumber() //
//						+ " time: " + number.getProcessingTime() //
//						+ " node: " + number.getProcessedBy() //
//						+ " fib:" + number.getResult()); 
//			}

		} catch (Exception e) {
			e.printStackTrace();// TODO: handle exception
		} finally {
			client.shutdown();

		}
	}
	
	private void div (HazelcastInstance instance) {
		  IMap imap = (IMap) instance.getMap(MAP_NAME);
		  Config config = instance.getConfig();
		  MapConfig mConfig = config.getMapConfig(imap.getName());
		  System.out.println("capacity: " + mConfig.getMaxSizeConfig().getSize());
		  System.out.println("current capacity" + imap.size());
		  LocalMapStats localMapStats = imap.getLocalMapStats();
	}
	
}
