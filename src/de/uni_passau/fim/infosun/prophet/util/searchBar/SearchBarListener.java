package de.uni_passau.fim.infosun.prophet.util.searchBar;

/**
 * Classes implementing this interface can be registered to receive events from either <code>SearchBar</code> or
 * <code>GlobalSearchBar</code>.
 */
public interface SearchBarListener {

    /**
     * Called by the <code>SearchBar</code> or <code>GlobalSearchBar</code> after a search operation was finished.
     * The type of search (e.g. forward, backward, global) will be given as the <code>action</code> argument.
     *
     * @param action one of the <code>ACTION_<em>NAME</em></code> constants from <code>SearchBar</code> or
     *               <code>GlobalSearchBar</code> identifying the search action that was performed
     * @param query the <code>String</code> that was searched for
     * @param success whether <code>query</code> was found
     */
    public void searched(String action, String query, boolean success);
}
