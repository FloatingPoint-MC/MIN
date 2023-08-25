package net.minecraft.util.text.event;

import com.google.common.collect.Maps;
import java.util.Map;

public class ClickEvent
{
    private final Action action;
    private final String value;

    public ClickEvent(Action theAction, String theValue)
    {
        this.action = theAction;
        this.value = theValue;
    }

    /**
     * Gets the actions to perform when this event is raised.
     */
    public Action getAction()
    {
        return this.action;
    }

    /**
     * Gets the value to perform the actions on when this event is raised.  For example, if the actions is "open URL",
     * this would be the URL to open.
     */
    public String getValue()
    {
        return this.value;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
        {
            ClickEvent clickevent = (ClickEvent)p_equals_1_;

            if (this.action != clickevent.action)
            {
                return false;
            }
            else
            {
                if (this.value != null)
                {
                    return this.value.equals(clickevent.value);
                }
                else return clickevent.value == null;
            }
        }
        else
        {
            return false;
        }
    }

    public String toString()
    {
        return "ClickEvent{actions=" + this.action + ", value='" + this.value + '\'' + '}';
    }

    public int hashCode()
    {
        int i = this.action.hashCode();
        i = 31 * i + (this.value != null ? this.value.hashCode() : 0);
        return i;
    }

    public enum Action
    {
        OPEN_URL("open_url", true),
        OPEN_FILE("open_file", false),
        RUN_COMMAND("run_command", true),
        SUGGEST_COMMAND("suggest_command", true),
        CHANGE_PAGE("change_page", true);

        private static final Map<String, Action> NAME_MAPPING = Maps.newHashMap();
        private final boolean allowedInChat;
        private final String canonicalName;

        Action(String canonicalNameIn, boolean allowedInChatIn)
        {
            this.canonicalName = canonicalNameIn;
            this.allowedInChat = allowedInChatIn;
        }

        public boolean shouldAllowInChat()
        {
            return this.allowedInChat;
        }

        public String getCanonicalName()
        {
            return this.canonicalName;
        }

        public static Action getValueByCanonicalName(String canonicalNameIn)
        {
            return NAME_MAPPING.get(canonicalNameIn);
        }

        static {
            for (Action clickevent$action : values())
            {
                NAME_MAPPING.put(clickevent$action.getCanonicalName(), clickevent$action);
            }
        }
    }
}
