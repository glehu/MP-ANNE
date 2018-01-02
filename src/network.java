import java.util.Arrays;

/**
 * Created by duffy on 02.01.2018.
 */

class network
{
	private neuron[][] network; // List of neurons in the network [#layers][#neurons per layer]

	public network()
	{
		System.out.println(">>> network()\n");

		int[] shape = new int[]{1, 2, 1};    // 1 input neuron 2 neurons in 1st hidden layer 1 output neuron

		setupNetwork(shape);
	}

	private void setupNetwork(int[] shape)
	{
		int numNeurons = 0; // Total neurons in the network
		int numConnections = 0; // Total connections

		System.out.println(String.format(">>> setupNetwork(%s)\n", Arrays.toString(shape)));
		neuron n; // Neuron to be filled into the network (temp object)
		connection c; // Connection (link) between the neurons

		int layers = shape.length;
		network = new neuron[layers][];

		for (int i = 0; i < layers; i++) // Layers
		{
			int neurons = shape[i];
			System.out.println(String.format("\tLayer %d: %d Neurons", i, neurons));
			network[i] = new neuron[neurons];

			for (int j = 0; j < neurons; j++) // Filling the layers
			{
				if (i == 0) // Input neuron
				{
					n = new neuron(i, j, 1);
					System.out.println("\t\t(INPUT) added.");
				} else if (i == layers - 1) // Output neuron
				{
					n = new neuron(i, j, true);
					System.out.println("\t\t(OUTPUT) added.");
				} else // Hidden neuron
				{
					n = new neuron(i, j);
					System.out.println("\t\t(HIDDEN) added.");
				}

				network[i][j] = n; // Adding the neuron to the network
				numNeurons++;

				if (i > 0) // Connecting the neuron to all neurons in the previous layer
				{
					for (int prev = 0; prev < network[i - 1].length; prev++)
					{
						neuron previous = network[i - 1][prev];
						c = new connection(previous, n);
						numConnections++;

						previous.out.add(c);
						n.in.add(c);

						System.out.println(String.format("\t\t\t(%d/%d) connected to (%d/%d)",
								previous.layer, previous.pos, n.layer, n.pos));
					}
				}
			}
		}
		System.out.println(String.format("\tTotal neurons:\t\t%d\n\tTotal connections:\t%d",
				numNeurons, numConnections));
	}
}