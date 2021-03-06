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
package de.learnlib.oracles;

import de.learnlib.api.MembershipOracle;
import de.learnlib.api.Query;
import de.learnlib.api.SUL;
import java.util.Collection;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;

/**
 * A wrapper around a system under learning (SUL).
 * 
 * @author falkhowar
 */
public class SULOracle<I, O> implements MembershipOracle<I, Word<O>> {

	private final SUL<I, O> sul;

	public SULOracle(SUL<I, O> sul) {
		this.sul = sul;
	}

	@Override
	public void processQueries(Collection<? extends Query<I, Word<O>>> queries) {
		for (Query<I, Word<O>> query : queries) {
			answerQuery(query);
		}
	}

	private void answerQuery(Query<I, Word<O>> query) {
		sul.reset();
		// Prefix: Execute symbols, don't record output
		for(I sym : query.getPrefix())
			sul.step(sym);
		
		// Suffix: Execute symbols, outputs constitute output word
		Word<I> suffix = query.getSuffix();
		WordBuilder<O> wb = new WordBuilder<>(suffix.length());
		for(I sym : suffix)
			wb.add(sul.step(sym));
		
		query.answer(wb.toWord());
	}

}
