/* Copyright (C) 2013 TU Dortmund
 * This file is part of LearnLib, http://www.learnlib.de/.
 * 
 * LearnLib is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 3.0 as published by the Free Software Foundation.
 * 
 * LearnLib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with LearnLib; if not, see
 * <http://www.gnu.de/documents/lgpl.en.html>.
 */
package de.learnlib.algorithms.lstargeneric;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.automatalib.automata.fsa.DFA;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Symbol;

import org.testng.annotations.Test;

import de.learnlib.algorithms.lstargeneric.ce.ObservationTableCEXHandler;
import de.learnlib.algorithms.lstargeneric.closing.ClosingStrategy;
import de.learnlib.algorithms.lstargeneric.dfa.ExtensibleLStarDFA;
import de.learnlib.api.EquivalenceOracle;
import de.learnlib.api.LearningAlgorithm;
import de.learnlib.api.MembershipOracle;
import de.learnlib.cache.dfa.DFACacheOracle;
import de.learnlib.eqtests.basic.SimulatorEQOracle;
import de.learnlib.eqtests.basic.WMethodEQOracle;
import de.learnlib.eqtests.basic.WpMethodEQOracle;
import de.learnlib.examples.dfa.ExamplePaulAndMary;
import de.learnlib.oracles.SimulatorOracle;

public class LStarDFATest extends LearningTest {

 
	@Test
	public void testLStar() {
		DFA<?,Symbol> targetDFA = ExamplePaulAndMary.getInstance();
		Alphabet<Symbol> alphabet = ExamplePaulAndMary.getInputAlphabet();
		
		MembershipOracle<Symbol, Boolean> dfaOracle = new SimulatorOracle<>(targetDFA);

		
		// Empty set of suffixes => minimum compliant set
		List<Word<Symbol>> suffixes = Collections.emptyList();
		
		List<EquivalenceOracle<? super DFA<?,Symbol>,Symbol,Boolean>> eqOracles
				= new ArrayList<EquivalenceOracle<? super DFA<?,Symbol>,Symbol,Boolean>>();
		
		eqOracles.add(new SimulatorEQOracle<>(targetDFA));
		eqOracles.add(new WMethodEQOracle<>(3, dfaOracle));
		eqOracles.add(new WpMethodEQOracle<>(3, dfaOracle));
		
		for(ObservationTableCEXHandler<? super Symbol,? super Boolean> handler : LearningTest.CEX_HANDLERS) {
			for(ClosingStrategy<? super Symbol,? super Boolean> strategy : LearningTest.CLOSING_STRATEGIES) {
					
				for(EquivalenceOracle<? super DFA<?,Symbol>, Symbol, Boolean> eqOracle : eqOracles) {
					DFACacheOracle<Symbol> cache = new DFACacheOracle<>(alphabet, dfaOracle);
					LearningAlgorithm<? extends DFA<?,Symbol>,Symbol,Boolean> learner 
					= new ExtensibleLStarDFA<>(alphabet, cache, suffixes,
							handler, strategy);
					
					testLearnModel(targetDFA, alphabet, learner, dfaOracle, eqOracle);
				}
			}
		}
	}
	

}
