package net.minecraft.client.util;

public interface ITooltipFlag
{
    boolean isAdvanced();

    enum TooltipFlags implements ITooltipFlag
    {
        NORMAL(false),
        ADVANCED(true);

        final boolean isAdvanced;

        TooltipFlags(boolean advanced)
        {
            this.isAdvanced = advanced;
        }

        public boolean isAdvanced()
        {
            return this.isAdvanced;
        }
    }
}
