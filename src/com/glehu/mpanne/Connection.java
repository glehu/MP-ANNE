package com.glehu.mpanne;

import java.util.Random;

/**
 * Created by duffy on 02.01.2018.
 */

class Connection
{
	Neuron from, to;

	float weight;

	// Backward Propagation stuff
	float errorD; 		// Error derivative w/ respect to this connection's weight
	float errorD_total; // Total error derivative
	float numErrorD;	// Number of error derivatives

	Random rand = new Random();

	Connection(Neuron from, Neuron to)
	{
		this.from = from;
		this.to   = to;

		this.weight = rand.nextFloat() * (1 - -1) + -1; // Random weight between -1 and 1
	}
}

