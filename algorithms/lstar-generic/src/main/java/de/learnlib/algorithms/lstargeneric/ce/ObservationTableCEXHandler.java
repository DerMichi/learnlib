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
package de.learnlib.algorithms.lstargeneric.ce;

import java.util.List;

import net.automatalib.automata.concepts.SuffixOutput;
import de.learnlib.algorithms.lstargeneric.table.ObservationTable;
import de.learnlib.algorithms.lstargeneric.table.Row;
import de.learnlib.api.MembershipOracle;
import de.learnlib.oracles.DefaultQuery;

public interface ObservationTableCEXHandler<I, O> {
	public <RI extends I,RO extends O>
	List<List<Row<RI>>> handleCounterexample(DefaultQuery<RI,RO> ceQuery,
			ObservationTable<RI,RO> table,
			SuffixOutput<RI,RO> hypOutput, MembershipOracle<RI, RO> oracle);
	
	public boolean needsConsistencyCheck();
}
