package net.minecraft.util;

import javax.annotation.Nonnull;

public interface ITabCompleter
{
    /**
     * Sets the list of tab completions, as long as they were previously requested.
     */
    void setCompletions(@Nonnull String... newCompletions);
}
