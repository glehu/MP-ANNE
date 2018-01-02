/**
 * Created by duffy on 02.01.2018.
 */

class main
{
	public static void main (String[] arg)
	{
		network n = new network();

		// E.g. {1, 2, 1} 1 input neuron 2 neurons in 1st hidden layer 1 output neuron
		int[] shape = new int[]{2, 1};
		n.setupNetwork(shape);
		n.feedForward();
		// For more information see network.java (and all the other classes too)
	}
}
