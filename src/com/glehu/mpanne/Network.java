package com.glehu.mpanne;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by duffy on 02.01.2018.
 */

class Network extends JPanel
{
	private Neuron[][] network; // List of neurons in the network [#layers][#neurons per layer]

	public Network()
	{
		System.out.println(">>> network()\n");
	}

	// Creates a network of neurons and connections, with a user defined shape of the network (see network())
	void setupNetwork(int[] shape)
	{
		System.out.printf(">>> setupNetwork(%s)\n\n", Arrays.toString(shape));

		int numNeurons = 0; // Total neurons in the network
		int numConnections = 0; // Total connections

		Neuron n; // Neuron to be filled into the network
		Connection c; // Connection (link) between two neurons

		int layers = shape.length;
		network = new Neuron[layers][];

		for (int i = 0; i < layers; i++) // Layers
		{
			int neurons = shape[i];
			System.out.printf("\tLayer %d: %d Neurons\n", i, neurons);
			network[i] = new Neuron[neurons];

			for (int j = 0; j < neurons; j++) // Filling the layers
			{
				if (i == 0) // First layer -> Input neuron
				{
					n = new Neuron(i, j);
					//System.out.printf("\t\tINPUT  (%d/%d) added. (Value: %f)\n",
					//n.layer, n.pos, n.totalInput);
				} else if (i == layers - 1) // Last layer -> Output neuron
				{
					n = new Neuron(i, j);
					//System.out.printf("\t\tOUTPUT (%d/%d) added.\n", n.layer, n.pos);
				} else // Layer(s) between first and last -> Hidden neuron
				{
					n = new Neuron(i, j);
					//System.out.printf("\t\tHIDDEN (%d/%d) added.\n", n.layer, n.pos);
				}

				network[i][j] = n; // Adding the neuron to the network
				numNeurons++;

				if (i > 0) // Connecting the neuron to all neurons in the previous layer
				{
					for (int prev = 0; prev < network[i - 1].length; prev++)
					{
						Neuron previous = network[i - 1][prev];
						c = new Connection(previous, n);
						numConnections++;

						previous.out.add(c);
						n.in.add(c);

						//System.out.printf("\t\t\t(%d/%d) connected to (%d/%d) with weight %+f\n",
						//previous.layer, previous.pos, n.layer, n.pos, c.weight);
					}
				}
			}
		}
		System.out.printf("\n\tTotal neurons:\t\t%,d\n\tTotal connections:\t%,d\n\n",
				numNeurons, numConnections);
	}

	// Passes the values of the input neurons to the connected neurons with respect of the connections' weight
	private void feedForward()
	{
		for (int i = 1; i < network.length; i++)
		{
			Neuron[] currentLayer = network[i];
			for (Neuron n : currentLayer)
			{
				n.calculate();
			}
		}
	}

	// Backward Propagation
	// Here, the error of the neurons and connections are calculated and the weights get updated
	private void backProp(float truth)
	{
		// Calculating the error of the output neurons
		for (int outputNeurons = 0; outputNeurons < network[network.length - 1].length; outputNeurons++)
		{
			Neuron n = network[network.length - 1][outputNeurons];

			if (n.pos == truth)
			{
				n.outputD = 1 - n.output;
			} else
			{
				n.outputD = 0 - n.output;
			}
		}

		for (int i = network.length - 1; i >= 1; i--)
		{
			// Node input error derivatives
			for (int j = 0; j < network[i].length; j++)
			{
				Neuron n = network[i][j];

				n.inputD = n.outputD * n.relu(true, n.totalInput);
				n.inputD_total += n.inputD;
				n.numInputD++;
			}

			// Connection error derivatives
			for (int j = 0; j < network[i].length; j++)
			{
				Neuron n = network[i][j];
				for (Connection c : n.in)
				{
					c.errorD = n.inputD * c.from.output;
					c.errorD_total += c.errorD;
					c.numErrorD++;
				}
			}

			// Error derivative with respect to each node's output.
			if (i == 1) // Input neurons don't need to calculate the error (they can't be "wrong")
			{
				continue;
			}
			Neuron[] prevLayer = network[i - 1];
			for (Neuron n : prevLayer)
			{
				n.outputD = 0;
				for (Connection c : n.out)
				{
					n.outputD += c.weight * c.to.inputD;
				}
			}
		}
	}

	// Update the connections' weights and the nodes' bias to approach (truth - actual = 0)
	// The learningRate describes how strong the weights' adjustment is.
	// It is recommended to use a higher learningRate at the beginning that decreases with each epoch.
	// This allows for a rapid leaning process at the beginning and fine adjustments at the end.
	private void updateWeights(float learningRate, int minibatch_size)
	{
		for (int i = 1; i < network.length; i++)
		{
			Neuron[] currentLayer = network[i];
			for (Neuron n : currentLayer)
			{
				if (n.numInputD > 0)
				{
					n.bias -= learningRate * ((n.inputD_total / minibatch_size) / n.numInputD);
					n.inputD_total = 0;
					n.numInputD = 0;
				}
				for (Connection c : n.in)
				{
					if (c.numErrorD > 0)
					{
						c.weight = c.weight + learningRate * ((c.errorD_total / minibatch_size) / c.numErrorD);
						c.errorD_total = 0;
						c.numErrorD = 0;
					}
				}
			}
		}

		for (Neuron[] aNetwork : network)
		{
			for (Neuron n : aNetwork)
			{
				n.totalInput = n.bias;
			}
		}
	}

	void learn(int epochs, float learningRate, int minibatch_size, float error_aim, File[] dir)
	{
		randomImage randomImg = new randomImage(dir);
		int truth;

		int n0 = 0, n1 = 0, n2 = 0, n3 = 0, n4 = 0, n5 = 0, n6 = 0, n7 = 0, n8 = 0, n9 = 0;

		for (int i = 0; i < epochs; i++)
		{
			// Create imageInput object to return every pixel from the image for the input neurons
			randomImg.randImg();
			BufferedImage rand_img = randomImg.getRandImg();
			ImageInput ip = new ImageInput(rand_img);

			// Set truth according to selected Image class (0-9)
			truth = randomImg.getTruth();

			switch (truth)
			{
				case 0:
					n0++;
					break;
				case 1:
					n1++;
					break;
				case 2:
					n2++;
					break;
				case 3:
					n3++;
					break;
				case 4:
					n4++;
					break;
				case 5:
					n5++;
					break;
				case 6:
					n6++;
					break;
				case 7:
					n7++;
					break;
				case 8:
					n8++;
					break;
				case 9:
					n9++;
					break;
			}

			// Set Input values
			Neuron[] inputNeuron = network[0]; // Array of input neurons
			for (Neuron n : inputNeuron)
			{
				n.totalInput = ip.getInput(n.pos);
			}

			float error = 0;

			feedForward();
			backProp(truth);

			if (i % minibatch_size == 0)
			{
				updateWeights(learningRate, minibatch_size);
				learningRate = learningRate * 0.9f;

				System.out.printf("Amount of each image class:\n" +
								"0: %d\n1: %d\n2: %d\n3: %d\n4: %d\n5: %d\n6: %d\n7: %d\n8: %d\n9: %d\n",
						n0, n1, n2, n3, n4, n5, n6, n7, n8, n9);
				n0 = 0;
				n1 = 0;
				n2 = 0;
				n3 = 0;
				n4 = 0;
				n5 = 0;
				n6 = 0;
				n7 = 0;
				n8 = 0;
				n9 = 0;
			}

			for (int out = 0; out < network[network.length - 1].length; out++)
			{
				Neuron n = network[network.length - 1][out];
				//System.out.printf("\n(%d/%d) ERROR: %+f\nOutput %+f\n", n.layer, n.pos, n.outputD, n.output);

				error += abs(n.outputD);
			}

			// If total error is below threshold exit learning process
			if (i > (minibatch_size * 2) && abs(error) <= error_aim)
			{
				break;
			}

			if (i % minibatch_size == 0 || i + 1 == epochs)
			{
				System.out.printf("Epoch %d/%d \tTOTAL ERROR: %f\n\n", i, epochs, error);
			}
		}

		File f = new File("C:\\Users\\duffy\\Desktop\\training\\test\\img_1.jpg"); // Testing a 0
		truth = 0;
		ImageInput i = null;
		try
		{
			i = new ImageInput(ImageIO.read(f));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		Neuron[] inputNeuron = network[0]; // Array of input neurons
		for (Neuron n : inputNeuron)
		{
			assert i != null;
			n.totalInput = i.getInput(n.pos);
		}

		feedForward();
		backProp(truth);

		float truth_;

		for (int out = 0; out < network[network.length - 1].length; out++)
		{
			Neuron n = network[network.length - 1][out];

			if (n.pos == truth)
			{
				truth_ = 1;
			} else
			{
				truth_ = 0;
			}

			System.out.printf("\n\t(%d/%d) ERROR: %+f\n\tACTUAL: %+f TRUTH: %+f\n\tBIAS: %+f\n",
					n.layer, n.pos, n.outputD, n.output, truth_, n.bias);
		}
	}
}