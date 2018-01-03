import java.util.ArrayList;

/**
 * Created by duffy on 02.01.2018.
 */

class neuron
{
	ArrayList<connection> in, out;

	float totalInput;
	float output;
	float bias;

	// Backward Propagation stuff
	float outputD;      // Error derivative w/ respect to node output
	float inputD;       // Error derivative w/ respect to node input
	float inputD_total; // Total error derivative 	   w/ respect to node input
	float numInputD;    // Number of error derivatives w/ respect to node input

	private boolean is_input, is_output;

	int layer; // X
	int pos;   // Y

	// -------------------------------------------

	neuron(int layer, int pos, int value)      // INPUT
	{
		this.in  = new ArrayList<>();
		this.out = new ArrayList<>();

		this.output = value;
		this.bias = 0.1f;

		this.is_input  = true;
		this.is_output = false;

		this.layer = layer;
		this.pos   = pos;
	}

	neuron(int layer, int pos)               // HIDDEN
	{
		this.in  = new ArrayList<>();
		this.out = new ArrayList<>();

		this.totalInput = 0;
		this.bias = 0.1f;

		this.is_input  = false;
		this.is_output = false;

		this.layer = layer;
		this.pos   = pos;
	}

	neuron(int layer, int pos, boolean output) // OUTPUT
	{
		this.in  = new ArrayList<>();
		this.out = new ArrayList<>();

		this.totalInput = 0;
		this.bias = 0.1f;

		this.is_input  = false;
		this.is_output = true;

		this.layer = layer;
		this.pos   = pos;
	}

	// ---------------------------------------------

	float relu(boolean derivative, float value)
	{
		if (derivative)
		{
			return 1;
		} else
		{
			return Math.max(0, value);
		}
	}

	float calculate()
	{
		totalInput = bias;
		for(connection c : in)
		{
			totalInput += c.weight * c.from.output;
		}
		output = relu(false, totalInput);
		return output;
	}
}

