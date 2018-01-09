import java.io.File;

/**
 * Created by duffy on 02.01.2018.
 */

class setup
{
	public static void main(String[] arg)
	{
		network n = new network();

		// E.g. {1, 2, 1} 1 input neuron 2 neurons in 1st hidden layer 1 output neuron
		int[] shape = new int[]{784, 16, 16, 10};
		n.setupNetwork(shape);
		File ordner = new File("C:\\Users\\duffy\\Desktop\\training\\3"); // Path to training data
		if (!ordner.exists() || !ordner.isDirectory())
		{
			System.err.println("No folder @ " + ordner);
			System.exit(1);
		}

		n.learn(1000, 3, 0.03f, 0.001f, ordner);
	}
} // For more information see network.java (and all the other classes too)
