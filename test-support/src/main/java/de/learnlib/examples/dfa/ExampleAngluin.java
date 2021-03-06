/* Copyright (C) 2013 TU Dortmund
 * This file is part of AutomataLib, http://www.automatalib.net/.
 * 
 * AutomataLib is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 3.0 as published by the Free Software Foundation.
 * 
 * AutomataLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with AutomataLib; if not, see
 * <http://www.gnu.de/documents/lgpl.en.html>.
 */

package de.learnlib.examples.dfa;

import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.MutableDFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.Alphabets;

/**
 * This class provides the example used in the paper ''Learning Regular Sets
 * from Queries and Counterexamples'' by Dana Angluin that consists of an
 * automaton that accepts ''all strings over {0,1} with an even number of 0's
 * and an even number of 1's.''
 * 
 * @author Oliver Bauer <oliver.bauer@tu-dortmund.de>
 */
public class ExampleAngluin {
	
	private static final class InstanceHolder {
		public static final DFA<?,Integer> INSTANCE;
		
		static {
			INSTANCE = constructMachine();
		}
	}
	
	private static final Alphabet<Integer> ALPHABET = Alphabets.integers(0, 1);

	
	public static Alphabet<Integer> getInputAlphabet() {
		return ALPHABET;
	}
	
	public static DFA<?,Integer> getInstance() {
		return InstanceHolder.INSTANCE;
	}
	
	
	public static <A extends MutableDFA<S, ? super Integer>,S>
	A constructMachine(A machine) {
		S q0 = machine.addInitialState(true);
		S q1 = machine.addState(false), q2 = machine.addState(false), q3 = machine.addState(false);
		
		machine.addTransition(q0, 0, q1);
		machine.addTransition(q0, 1, q2);
		
		machine.addTransition(q1, 0, q0);
		machine.addTransition(q1, 1, q3);
		
		machine.addTransition(q2, 0, q3);
		machine.addTransition(q2, 1, q0);
		
		machine.addTransition(q3, 0, q2);
		machine.addTransition(q3, 1, q1);
		
		return machine;
	}
	
	public static CompactDFA<Integer> constructMachine() {
		return constructMachine(new CompactDFA<>(ALPHABET));
	}
	
	
	
	
	
}
