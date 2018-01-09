import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by duffy on 02.01.2018.
 */

class network
{
	private neuron[][] network; // List of neurons in the network [#layers][#neurons per layer]

	public network()
	{
		System.out.println(">>> network()\n");
	}

	// Creates a network of neurons and connections, with a user defined shape of the network (see network())
	void setupNetwork(int[] shape)
	{
		System.out.printf(">>> setupNetwork(%s)\n\n", Arrays.toString(shape));

		int numNeurons = 0; // Total neurons in the network
		int numConnections = 0; // Total connections

		neuron n; // Neuron to be filled into the network
		connection c; // Connection (link) between two neurons

		int layers = shape.length;
		network = new neuron[layers][];

		for (int i = 0; i < layers; i++) // Layers
		{
			int neurons = shape[i];
			System.out.printf("\tLayer %d: %d Neurons\n", i, neurons);
			network[i] = new neuron[neurons];

			for (int j = 0; j < neurons; j++) // Filling the layers
			{
				if (i == 0) // First layer -> Input neuron
				{
					n = new neuron(i, j);
					System.out.printf("\t\tINPUT  (%d/%d) added. (Value: %f)\n",
							n.layer, n.pos, n.totalInput);
				} else if (i == layers - 1) // Last layer -> Output neuron
				{
					n = new neuron(i, j);
					System.out.printf("\t\tOUTPUT (%d/%d) added.\n", n.layer, n.pos);
				} else // Layer(s) between first and last -> Hidden neuron
				{
					n = new neuron(i, j);
					System.out.printf("\t\tHIDDEN (%d/%d) added.\n", n.layer, n.pos);
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

						System.out.printf("\t\t\t(%d/%d) connected to (%d/%d) with weight %+f\n",
								previous.layer, previous.pos, n.layer, n.pos, c.weight);
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
			neuron[] currentLayer = network[i];
			for (neuron n : currentLayer)
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
			neuron n = network[network.length - 1][outputNeurons];

			if(n.pos == truth)
			{
				n.outputD = truth - n.output;
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
				neuron n = network[i][j];

				n.inputD = n.outputD * n.relu(true, n.totalInput);
				n.inputD_total += n.inputD;
				n.numInputD++;
			}

			// Connection error derivatives
			for (int j = 0; j < network[i].length; j++)
			{
				neuron n = network[i][j];
				for (connection c : n.in)
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
			neuron[] prevLayer = network[i - 1];
			for (neuron n : prevLayer)
			{
				n.outputD = 0;
				for (connection c : n.out)
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
	private void updateWeights(float learningRate)
	{
		for (int i = 1; i < network.length; i++)
		{
			neuron[] currentLayer = network[i];
			for (neuron n : currentLayer)
			{
				if (n.numInputD > 0)
				{
					n.bias -= learningRate * n.inputD_total / n.numInputD;
					n.inputD_total = 0;
					n.numInputD = 0;
				}
				for (connection c : n.in)
				{
					if (c.numErrorD > 0)
					{
						c.weight = c.weight + (learningRate / c.numErrorD) * c.errorD_total;
						c.errorD_total = 0;
						c.numErrorD = 0;
					}
				}
			}
		}

		for (neuron[] aNetwork : network)
		{
			for (neuron n : aNetwork)
			{
				n.totalInput = n.bias;
			}
		}
	}

	void learn(int epochs, float truth, float learningRate, float error_aim, File dir)
	{
		for (int i = 0; i < epochs; i++)
		{
			// Create imageInput object to return every pixel from the image for the input neurons
			imageInput ip = new imageInput(randomImage(dir));

			// Set Input values
			neuron[] inputNeuron = network[0]; // Array of input neurons
			for(neuron n : inputNeuron)
			{
				n.totalInput = ip.getInput(n.pos);
			}

			float error = 0;

			feedForward();
			backProp(truth);
			updateWeights(learningRate);


			for (int out = 0; out < network[network.length - 1].length; out++)
			{
				neuron n = network[network.length - 1][out];
				//System.out.printf("\n(%d/%d) ERROR: %+f\nOutput %+f\n", n.layer, n.pos, n.outputD, n.output);

				error += abs(n.outputD);
			}


			// If total error is below threshold exit learning process
			if (abs(error) <= error_aim)
			{
				float truth_;

				for (int out = 0; out < network[network.length - 1].length; out++)
				{
					neuron n = network[network.length - 1][out];

					if(n.pos == truth)
					{
						truth_ = truth;
					} else
					{
						truth_ = 0;
					}

					System.out.printf("\n\t(%d/%d) ERROR: %+f\n\tACTUAL: %+f TRUTH: %+f\n\tBIAS: %+f\n",
							n.layer, n.pos, n.outputD, n.output, truth_, n.bias);
				}

				System.out.printf("\n\n\tTotal Error <0.001 @ epoch %d\n", i);
				break;
			}

			System.out.printf("Epoch %d/%d \tTOTAL ERROR: %f\n\n", i, epochs, error);
		}
	}

	private BufferedImage randomImage(File dir)
	{
		BufferedImage img = null;
		File[] files = dir.listFiles();
		Random rand = new Random();
		assert files != null;
		try
		{
			File image = files[rand.nextInt(files.length)];
			System.out.println(image);
			if (image.isDirectory())
			{
				return randomImage(dir);
			}
			img = ImageIO.read(image);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(2);
		}

		return img;
	}


}