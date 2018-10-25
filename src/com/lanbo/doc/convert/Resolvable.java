package com.lanbo.doc.convert;

/**
 * Resolvable is an interface for those Java types that cannot resolve, upon
 * first parsing the file in which the type is declared, certain types
 * referenced by that file.
 * 
 * <p>
 * This interface provides a standard means of saving {@link Resolution}s that
 * we will later resolve once we have parsed every file. This is provided via
 * the {@link addResolution(Resolution)} method.
 * 
 * <p>
 * Additionally, This interface provides a standard means of resolving all
 * resolutions via the {@link#resolveResolutions()} method.
 */
public interface Resolvable {
	/**
	 * Adds a {@link Resolution} that will be resolved at a later time.
	 * 
	 * @param resolution
	 *            The {@link Resolution} to resolve at a later time.
	 */
	public void addResolution(Resolution resolution);

	/**
	 * Resolves the {@link Resolution}s contained in this {@link Resolvable}.
	 * 
	 * @return <tt>true</tt> if all resolutions were resolved. <tt>false</tt> if
	 *         there are still remaining resolutions.
	 */
	public boolean resolveResolutions();

	/**
	 * Prints the list of {@link Resolution}s that will be resolved at a later
	 * time.
	 */
	public void printResolutions();
}
