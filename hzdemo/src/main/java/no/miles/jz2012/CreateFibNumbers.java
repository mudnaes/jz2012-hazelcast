package no.miles.jz2012;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.hazelcast.client.ClientConfig;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiTask;

public class CreateFibNumbers {
	private static int START_POINT = 20000;
	private static int NUMBER_OF_FIBS = 1000;
	private static String MAP_NAME = "two-backup";

	public static void main(String[] args) {

		HazelcastInstance client = setupHzClient();
		Map<Integer, FibonacciNumber> fibNumbers = createFibSequence(client);
		calculateFibs(client, fibNumbers);

		client.shutdown();
	}

	private static void calculateFibs(HazelcastInstance client,
			Map<Integer, FibonacciNumber> fibNumbers) {
		long startTime;

		
		startTime = System.nanoTime();

		MultiTask<String> task = new MultiTask<String>(new CalculatorTask(
				MAP_NAME), client.getCluster().getMembers());

		ExecutorService executorService = client.getExecutorService();
		executorService.execute(task);

		try {
			Collection<String> results = task.get();

			for (String memberResult : results) {
				System.out.println("member:" + memberResult);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.printf(" Calculation time: %d ms%n",
				(System.nanoTime() - startTime) / 1000000);
	}

	private static Map<Integer, FibonacciNumber> createFibSequence(
			HazelcastInstance client) {
		Map<Integer, FibonacciNumber> fibNumbers = client.getMap(MAP_NAME);
		long startTime = System.nanoTime();

		for (int i = START_POINT; i < START_POINT + NUMBER_OF_FIBS; i++) {
			fibNumbers.put(i, new FibonacciNumber(i));
		}

		System.out.println(" Adding " + NUMBER_OF_FIBS + " numbers to map in  "
				+ ((System.nanoTime() - startTime) / 1000000) + " ms");
		return fibNumbers;
	}

	private static HazelcastInstance setupHzClient() {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.getGroupConfig().setName("dev").setPassword("dev-pass");
		clientConfig.addAddress("localhost");
		HazelcastInstance client = HazelcastClient
				.newHazelcastClient(clientConfig);
		return client;
	}
}
