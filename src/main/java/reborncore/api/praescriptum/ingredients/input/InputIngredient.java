/*
 * Copyright (c) 2018 modmuss50 and Gigabit101
 *
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package reborncore.api.praescriptum.ingredients.input;

import reborncore.api.praescriptum.ingredients.Ingredient;

/**
 * @author estebes
 */
public abstract class InputIngredient<T> extends Ingredient<T> {
	protected InputIngredient(T ingredient) {
		this(ingredient, true);
	}

	protected InputIngredient(T ingredient, boolean consumable) {
		super(ingredient);

		this.consumable = consumable;
	}

	public abstract Object getUnspecific();

	public abstract InputIngredient<T> copy();

	public abstract int getCount();

	public abstract void shrink(int amount);

	public boolean isConsumable() {
		return consumable;
	}

	@Override
	public int hashCode() {
//		return Objects.hash(ingredient, consumable);
		return 1;
	}

	@Override
	public boolean equals(Object object) {
		if (getClass() != object.getClass()) return false;

		return matches(((InputIngredient) object).ingredient) && this.consumable == ((InputIngredient) object).consumable;
	}

	// Fields >>
	public final boolean consumable;
	// << Fields
}