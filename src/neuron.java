import java.util.ArrayList;

/**
 * Created by duffy on 02.01.2018.
 */

class neuron
{
	ArrayList<connection> in, out;
	public float value;
	public boolean input, output;

	int layer; // X
	int pos; // Y

	// -------------------------------------------

	neuron(int layer, int pos, int value)      // INPUT
	{
		this.in  = new ArrayList<connection>();
		this.out = new ArrayList<connection>();

		this.value = value;

		this.input  = true;
		this.output = false;

		this.layer = layer;
		this.pos   = pos;
	}

	neuron(int layer, int pos)               // HIDDEN
	{
		this.in  = new ArrayList<connection>();
		this.out = new ArrayList<connection>();

		this.value = 0;

		this.input  = false;
		this.output = false;

		this.layer = layer;
		this.pos   = pos;
	}

	neuron(int layer, int pos, boolean output) // OUTPUT
	{
		this.in  = new ArrayList<connection>();
		this.out = new ArrayList<connection>();

		this.value = 0;

		this.input  = false;
		this.output = true;

		this.layer = layer;
		this.pos   = pos;
	}

	// ---------------------------------------------

	void input(float x)
	{
		value += x;
	}

	private float relu(boolean derivative)
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

