import java.util.Arrays;

/**
 * Created by duffy on 02.01.2018.
 */
public class network
{
	neuron[][] network; // List of neurons in the network [#layers][#neurons per layer]

	public network()
	{
		System.out.println(">>> network()");

		int[] shape = new int[] {1, 2, 1};    // 1 input neuron 2 neurons in 1st hidden layer 1 output neuron

		setupNetwork(shape);
	}

	private void setupNetwork(int[] shape)
	{
		System.out.println(String.format(">>> setupNetwork(%s)", Arrays.toString(shape)));
		neuron n; // Neuron to be filled into the network (temp object)
		connection c; // Connection (link) between the neurons

		int layers = shape.length;
		network = new neuron[layers][];

		for (int i = 0; i < layers; i++) // Layers
		{
			int neurons = shape[i];
			System.out.println(String.format("Layer %d: %d Neurons", i, neurons));
			network[i] = new neuron[neurons];

			for (int j = 0; j < neurons; j++) // Filling the layers
			{
				if (i == 0) // Input neuron
				{
					n = new neuron(1);
					System.out.println("\t(INPUT) added.");
				} else if (i == layers - 1) // Output neuron
				{
					n = new neuron(true);
					System.out.println("\t(OUTPUT) added.");
				} else // Hidden neuron
				{
					n = new neuron();
					System.out.println("\t(HIDDEN) added.");
				}

				network[i][j] = n; // Adding the neuron to the network

				if(i > 0) // Connecting the neuron to all neurons in the previous layer
				{
					for(int prev = 0; prev < network[i-1].length; prev++)
					{
						neuron previous = network[i-1][prev];
						c = new connection(previous, n);

						previous.out.add(c);
						n.in.add(c);
					}
				}
			}
		}
	}
}