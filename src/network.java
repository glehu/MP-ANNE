import java.util.Arrays;

/**
 * Created by duffy on 02.01.2018.
 */
public class network
{
	neuron[][] network; // list of neurons in the network [#layers][#neurons per layer]

	public network()
	{
		System.out.println(">>> network()");

		int[] shape = new int[] {1, 2, 2, 1};    // 1 input neuron 2 neurons in 1st hidden layer 1 output neuron

		setupNetwork(shape);
	}

	private void setupNetwork(int[] shape)
	{
		System.out.println(String.format(">>> setupNetwork(%s)", Arrays.toString(shape)));
		neuron n; // Neuron to be filled into the network (temp object)

		int layers = shape.length;
		network = new neuron[layers][];

		for (int i = 0; i < layers; i++)
		{
			int neurons = shape[i];
			System.out.println(String.format("Layer %d: %d Neurons", i, neurons));
			network[i] = new neuron[neurons];

			for (int j = 0; j < neurons; j++)
			{
				if (i == 0) // Input neuron
				{
					n = new neuron(1);
				} else if (i == layers - 1) // Output neuron
				{
					n = new neuron(true);
				} else // Hidden neuron
				{
					n = new neuron();
				}

				network[i][j] = n;
			}
		}

		n = null;
	}
}