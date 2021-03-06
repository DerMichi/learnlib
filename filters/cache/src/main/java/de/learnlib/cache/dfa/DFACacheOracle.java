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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.automatalib.incremental.dfa.Acceptance;
import net.automatalib.incremental.dfa.IncrementalDFABuilder;
import net.automatalib.words.Alphabet;
import de.learnlib.api.MembershipOracle;
import de.learnlib.api.Query;


/**
 * DFA cache. This cache is implemented as a membership oracle: upon construction, it is
 * provided with a delegate oracle. Queries that can be answered from the cache are answered
 * directly, others are forwarded to the delegate oracle. When the delegate oracle has finished
 * processing these remaining queries, the results are incorporated into the cache.
 * 
 * @author Malte Isberner <malte.isberner@gmail.com>
 *
 * @param <I> input symbol class
 */
public class DFACacheOracle<I> implements MembershipOracle<I, Boolean> {
	
	private final IncrementalDFABuilder<I> incDfa;
	private final MembershipOracle<I,Boolean> delegate;

	/**
	 * Constructor.
	 * @param alphabet the alphabet of the cache
	 * @param delegate the delegate oracle
	 */
	public DFACacheOracle(Alphabet<I> alphabet, MembershipOracle<I,Boolean> delegate) {
		this.incDfa = new IncrementalDFABuilder<>(alphabet);
		this.delegate = delegate;
	}
	
	public int getCacheSize() {
		return incDfa.size();
	}
	
	/**
	 * Creates an equivalence oracle that checks an hypothesis for consistency with the
	 * contents of this cache. Note that the returned oracle is backed by the cache data structure,
	 * i.e., it is sufficient to call this method once after creation of the cache.
	 * @return the cache consistency test backed by the contents of this cache.
	 */
	public DFACacheConsistencyTest<I> createCacheConsistencyTest() {
		return new DFACacheConsistencyTest<>(incDfa);
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.learnlib.api.MembershipOracle#processQueries(java.util.Collection)
	 */
	@Override
	public void processQueries(Collection<? extends Query<I, Boolean>> queries) {
		List<ProxyQuery<I>> unanswered = new ArrayList<>();
		
		for(Query<I,Boolean> q : queries) {
			Acceptance acc = incDfa.lookup(q.getInput());
			if(acc != Acceptance.DONT_KNOW)
				q.answer((acc == Acceptance.TRUE) ? true : false);
			else
				unanswered.add(new ProxyQuery<>(q));
		}
		
		delegate.processQueries(unanswered);
		
		for(ProxyQuery<I> q : unanswered)
			incDfa.insert(q.getInput(), q.getAnswer());
	}

}
