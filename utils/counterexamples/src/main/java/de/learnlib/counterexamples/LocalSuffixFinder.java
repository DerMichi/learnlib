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
package de.learnlib.counterexamples;

import net.automatalib.automata.concepts.SuffixOutput;
import de.learnlib.api.AccessSequenceTransformer;
import de.learnlib.api.LearningAlgorithm;
import de.learnlib.api.MembershipOracle;
import de.learnlib.api.Query;

/**
 * Suffix-based local counterexample analyzer.
 * <p>
 * Given a query <tt>(u, v)</tt> which is a counterexample (i.e., the suffix-output for
 * <tt>(u,v)</tt> is distinct from the target system's output for <tt>(u,v)</tt>), it
 * calculates the index <tt>i</tt> of the suffix such that <tt>w[i:]</tt> (<tt>w = uv</tt>)
 * still allows to expose a behavioral difference for an adequate prefix. This adequate prefix
 * can be determined as <tt>{w[:i]}</tt>, where <tt>{.}</tt> denotes the access sequence of
 * the corresponding word.
 * <p>
 * The effect of adding such a suffix can be described as follows: <tt>{w[:i]}</tt> and
 * <tt>{w[:i-1]}w[i-1]</tt> both lead to the same state in the hypothesis, but a local suffix finder
 * chooses the index i such that the output for <tt>({w[:i]}, w[i:])</tt> and
 * <tt>({w[:i-1]}w[i-1], w[i:])</tt> will differ. Hence, the transition to the state reached by
 * <tt>{w[:i]}</tt> from <tt>{w[:i-1]}</tt> is disproved.
 * <p>
 * Please note that the type parameters of these class only constitute <i>upper</i> bounds
 * for the respective input symbol and output classes, denoting the requirements of the
 * process in general. A suffix finder which does not
 * exploit any properties of the used classes will implement this interface with
 * <tt>&lt;Object,Object&gt;</tt> generic arguments only. The genericity is still maintained
 * due to the <tt>RI</tt> and <tt>RO</tt> generic parameters in the
 * {@link #findSuffixIndex(Query, AccessSequenceTransformer, SuffixOutput, MembershipOracle)}
 * method.
 * 
 * @author Malte Isberner <malte.isberner@gmail.com>
 *
 * @param <I> input symbol class upper bound
 * @param <O> output class upper bound
 */
public interface LocalSuffixFinder<I, O> {
	
	/** 
	 * Finds, for a given counterexample, a "split index", such that:
	 * - the part of the query word <i>before this index</i> leads to the state being split
	 * - the part of the query word <i>from this index on</i> is a suffix capable of splitting
	 * this state.
	 * 
	 * @param <RI> real input symbol class used for *this* counterexample analysis
	 * @param <RO> real output class used for *this* counterexample analysis
	 * @param ceQuery the counterexample query that triggered the refinement. Note that the same
	 * restrictions as in {@link LearningAlgorithm#refineHypothesis(de.learnlib.oracles.DefaultQuery)}
	 * apply.
	 * @param asTransformer an {@link AccessSequenceTransformer} used for access sequence transformation,
	 * if applicable.
	 * @param hypOutput interface to the output generation of the hypothesis, with the aim of
	 * comparing outputs of the hypothesis and the SUL.
	 * @param oracle interface to the System Under Learning (SUL).
	 * @return an adequate split index, or <tt>-1</tt> if the counterexample could not be analyzed.
	 */
	public <RI extends I,RO extends O>
	int findSuffixIndex(Query<RI,RO> ceQuery,
			AccessSequenceTransformer<RI> asTransformer,
			SuffixOutput<RI,RO> hypOutput,
			MembershipOracle<RI,RO> oracle);
	
}
