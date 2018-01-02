import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by duffy on 02.01.2018.
 */
public class connection
{
	neuron from, to;

	float weight;

	public connection(neuron from, neuron to)
	{
		this.from = from;
		this.to   = to;

		this.weight = ThreadLocalRandom.current().nextInt(-1, 1 + 1);
	}

	void feed(float x)
	{
		to.input(x);
	}
}
