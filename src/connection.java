import java.util.Random;

/**
 * Created by duffy on 02.01.2018.
 */

class connection
{
	private neuron from, to;

	float weight;

	connection(neuron from, neuron to)
	{
		this.from = from;
		this.to   = to;

		Random rand = new Random();
		this.weight = rand.nextFloat() * (1 - -1) + -1; // Random weight between -1 and 1
	}

	void feed(float x)
	{
		to.input(x * weight);
	}
}

