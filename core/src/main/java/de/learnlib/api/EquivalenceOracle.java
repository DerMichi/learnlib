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
package de.learnlib.api;

import java.util.Collection;

import de.learnlib.oracles.DefaultQuery;


/**
 * An equivalence oracle, which checks hypothesis automata against the (possibly unknown)
 * system under learning (SUL).
 * 
 * Please note that equivalence oracles are implicitly connected to a SUL, there is no explicit
 * references in terms of a {@link MembershipOracle} or such. However, this might be different
 * in implementing classes.
 * 
 * <b>CAVEAT:</b> Equivalence oracles serve as an abstraction to tackle the (generally undecidable)
 * problem of black-box equivalence testing. The contract imposed by this interface is that
 * results returned by the {@link #findCounterExample(Object, Collection)} method are in fact
 * counterexamples, <b>BUT</b> a <tt>null</tt> result signalling no counterexample was found
 * does <b>not</b> mean that there can be none.
 * 
 * @author Maik Merten <maikmerten@googlemail.com>
 * @author Malte Isberner <malte.isberner@gmail.com>
 * 
 * @param <A> automaton class this equivalence oracle works on
 * @param <I> input symbol class
 * @param <O> output class
 */
public interface EquivalenceOracle<A, I, O> {
	
	/**
	 * Searches for a counterexample disproving the subjected hypothesis.
	 * A counterexample is query which, when performed on the SUL, yields a different output
	 * than what was predicted by the hypothesis. If no counterexample could be found (this does
	 * not necessarily mean that none exists), <code>null</code> is returned.
	 * 
	 * @param hypothesis the conjecture
	 * @param inputs the set of inputs to consider
	 * @return a query exposing different behavior, or <tt>null</tt> if no counterexample
	 * could be found. In case a non-<tt>null</tt> value is returned, the output field
	 * in the {@link DefaultQuery} contains the SUL output for the respective query.
	 */
	public DefaultQuery<I, O> findCounterExample(A hypothesis, Collection<? extends I> inputs);  
	
}
