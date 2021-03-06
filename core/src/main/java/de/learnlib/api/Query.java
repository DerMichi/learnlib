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

import net.automatalib.words.Word;

public abstract class Query<I, O> {
	
	private int hashCode = 0;
	
	public abstract Word<I> getPrefix();
	public abstract Word<I> getSuffix();
	
	public abstract void answer(O output);
	
	public final Word<I> getInput() {
		return getPrefix().concat(getSuffix());
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(!(o instanceof Query))
			return false;
		Query<?,?> other = (Query<?,?>)o;
		
		Word<I> thisPref = getPrefix();
		Word<I> thisSuff = getSuffix();
		
		Word<?> otherPref = other.getPrefix();
		Word<?> otherSuff = other.getSuffix();
		
		if(thisPref != otherPref && (thisPref == null || !thisPref.equals(otherPref)))
			return false;
		if(thisSuff != otherSuff && (thisSuff == null || !thisSuff.equals(otherSuff)))
			return false;
		
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		if(hashCode != 0)
			return hashCode;
		
		Word<I> prefix = getPrefix(), suffix = getSuffix();
		hashCode = 5;
        hashCode = 89 * hashCode + (prefix != null ? prefix.hashCode() : 0);
        hashCode = 89 * hashCode + (suffix != null ? suffix.hashCode() : 0);
        return hashCode;
	}
	
}
