package com.glehu.mpanne;

import java.io.File;

/**
 * Created by duffy on 02.01.2018.
 */

class Setup
{
	public static void main(String[] arg)
	{
		Network n = new Network();

		// E.g. {1, 2, 1} 1 input neuron 2 neurons in 1st hidden layer 1 output neuron
		int[] shape = new int[]{784, 16, 16, 10};
		n.setupNetwork(shape);

		File[] folders = new File[1];
		for(int dir = 0; dir < 1; dir++)
		{
			String dir_ = String.format("C:\\Users\\duffy\\Desktop\\training\\%d", dir);
			folders[dir] = new File(dir_);
			System.out.println(dir_);
		}

		n.learn(1000000, 0.3f, 200,0.0f, folders);
	}
} // For more information see network.java (and all the other classes too)
