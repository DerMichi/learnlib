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
package de.learnlib.cache.dfa;

import java.util.Collection;

import net.automatalib.automata.fsa.DFA;
import net.automatalib.incremental.dfa.AbstractIncrementalDFABuilder;
import net.automatalib.incremental.dfa.Acceptance;
import net.automatalib.incremental.dfa.IncrementalDFABuilder;
import net.automatalib.words.Word;
import de.learnlib.api.EquivalenceOracle;
import de.learnlib.oracles.DefaultQuery;

/**
 * An {@link EquivalenceOracle} that tests an hypothesis for consistency with the
 * contents of a {@link DFACacheOracle}.
 * 
 * @author Malte Isberner <malte.isberner@gmail.com>
 *
 * @param <I> input symbol class
 */
public class DFACacheConsistencyTest<I> implements
		EquivalenceOracle<DFA<?,I>, I, Boolean> {
	
	private final AbstractIncrementalDFABuilder<I> incDfa;
	
	/**
	 * Constructor.
	 * @param incDfa the {@link IncrementalDFABuilder} data structure of the cache
	 */
	public DFACacheConsistencyTest(AbstractIncrementalDFABuilder<I> incDfa) {
		this.incDfa = incDfa;
	}

	/*
	 * (non-Javadoc)
	 * @see de.learnlib.api.EquivalenceOracle#findCounterExample(java.lang.Object, java.util.Collection)
	 */
	@Override
	public DefaultQuery<I, Boolean> findCounterExample(DFA<?, I> hypothesis,
			Collection<? extends I> inputs) {
		Word<I> w = incDfa.findSeparatingWord(hypothesis, inputs, false);
		if(w == null)
			return null;
		Acceptance acc = incDfa.lookup(w);
		assert (acc != Acceptance.DONT_KNOW);
		
		Boolean out = (acc == Acceptance.TRUE) ? true : false;
		DefaultQuery<I,Boolean> result = new DefaultQuery<>(w);
		result.answer(out);
		return result;
	}

}

