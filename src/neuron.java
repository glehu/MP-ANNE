import java.util.ArrayList;

/**
 * Created by duffy on 02.01.2018.
 */
public class neuron
{
	ArrayList<connection> in, out;
	float value;
	boolean input, output;

	// -------------------------------------------

	public neuron(int value)      // INPUT
	{
		this.in  = new ArrayList<connection>();
		this.out = new ArrayList<connection>();

		this.value = value;

		this.input  = true;
		this.output = false;
	}

	public neuron()               // HIDDEN
	{
		this.in  = new ArrayList<connection>();
		this.out = new ArrayList<connection>();

		this.value = 0;

		this.input  = false;
		this.output = false;
	}

	public neuron(boolean output) // OUTPUT
	{
		this.in  = new ArrayList<connection>();
		this.out = new ArrayList<connection>();

		this.value = 0;

		this.input  = false;
		this.output = true;
	}

	// ---------------------------------------------

	void input(float x)
	{
		value += x;
	}

	float relu(boolean derivative)
	{
		if (derivative)
		{
			return 1;
		} else
		{
			return Math.max(0, value);
		}
	}
}

