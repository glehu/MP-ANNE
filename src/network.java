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

		// E.g. {1, 2, 1} 1 input neuron 2 neurons in 1st hidden layer 1 output neuron
		int[] shape = new int[]{1, 2, 1};

		setupNetwork(shape);

		feedForward();
	}

	private void setupNetwork(int[] shape)
	{
		System.out.println(String.format(">>> setupNetwork(%s)\n", Arrays.toString(shape)));

		int numNeurons = 0; // Total neurons in the network
		int numConnections = 0; // Total connections

		neuron n; // Neuron to be filled into the network
		connection c; // Connection (link) between two neurons

		int layers = shape.length;
		network = new neuron[layers][];

		for (int i = 0; i < layers; i++) // Layers
		{
			int neurons = shape[i];
			System.out.println(String.format("\tLayer %d: %d Neurons", i, neurons));
			network[i] = new neuron[neurons];

			for (int j = 0; j < neurons; j++) // Filling the layers
			{
				if (i == 0) // First layer -> Input neuron
				{
					n = new neuron(i, j, 1);
					System.out.println(String.format("\t\tINPUT  (%d/%d) added. (Value: %f)",
							n.layer, n.pos, n.value));
				} else if (i == layers - 1) // Last layer -> Output neuron
				{
					n = new neuron(i, j, true);
					System.out.println(String.format("\t\tOUTPUT (%d/%d) added.", n.layer, n.pos));
				} else // Layer(s) between first and last -> Hidden neuron
				{
					n = new neuron(i, j);
					System.out.println(String.format("\t\tHIDDEN (%d/%d) added.", n.layer, n.pos));
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

						System.out.println(String.format("\t\t\t(%d/%d) connected to (%d/%d) with weight %+f",
								previous.layer, previous.pos, n.layer, n.pos, c.weight));
					}
				}
			}
		}
		System.out.println(String.format("\n\tTotal neurons:\t\t%,d\n\tTotal connections:\t%,d",
				numNeurons, numConnections));
	}

	public void feedForward()
	{
		System.out.println("\n>>> feedForward()\n");

		for (neuron[] aNetwork : network)
		{
			for (neuron n : aNetwork)
			{
				n.feedForward();
			}
		}
	}
}