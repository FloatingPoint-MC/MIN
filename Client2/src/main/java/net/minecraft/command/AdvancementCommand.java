package net.minecraft.command;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class AdvancementCommand extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getName()
    {
        return "advancement";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getUsage(ICommandSender sender)
    {
        return "commands.advancement.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.advancement.usage");
        }
        else
        {
            ActionType advancementcommand$actiontype = ActionType.byName(args[0]);

            if (advancementcommand$actiontype != null)
            {
                if (args.length < 3)
                {
                    throw advancementcommand$actiontype.wrongUsage();
                }

                EntityPlayerMP entityplayermp = getPlayer(server, sender, args[1]);
                Mode advancementcommand$mode = Mode.byName(args[2]);

                if (advancementcommand$mode == null)
                {
                    throw advancementcommand$actiontype.wrongUsage();
                }

                this.perform(server, sender, args, entityplayermp, advancementcommand$actiontype, advancementcommand$mode);
            }
            else
            {
                if (!"test".equals(args[0]))
                {
                    throw new WrongUsageException("commands.advancement.usage");
                }

                if (args.length == 3)
                {
                    this.testAdvancement(sender, getPlayer(server, sender, args[1]), findAdvancement(server, args[2]));
                }
                else
                {
                    if (args.length != 4)
                    {
                        throw new WrongUsageException("commands.advancement.test.usage");
                    }

                    this.testCriterion(sender, getPlayer(server, sender, args[1]), findAdvancement(server, args[2]), args[3]);
                }
            }
        }
    }

    private void perform(MinecraftServer server, ICommandSender sender, String[] args, EntityPlayerMP player, ActionType p_193516_5_, Mode p_193516_6_) throws CommandException
    {
        if (p_193516_6_ == Mode.EVERYTHING)
        {
            if (args.length == 3)
            {
                int j = p_193516_5_.perform(player, server.getAdvancementManager().getAdvancements());

                if (j == 0)
                {
                    throw p_193516_6_.fail(p_193516_5_, player.getName());
                }
                else
                {
                    p_193516_6_.success(sender, this, p_193516_5_, player.getName(), j);
                }
            }
            else
            {
                throw p_193516_6_.usage(p_193516_5_);
            }
        }
        else if (args.length < 4)
        {
            throw p_193516_6_.usage(p_193516_5_);
        }
        else
        {
            Advancement advancement = findAdvancement(server, args[3]);

            if (p_193516_6_ == Mode.ONLY && args.length == 5)
            {
                String s = args[4];

                if (!advancement.getCriteria().containsKey(s))
                {
                    throw new CommandException("commands.advancement.criterionNotFound", advancement.getId(), args[4]);
                }

                if (!p_193516_5_.performCriterion(player, advancement, s))
                {
                    throw new CommandException(p_193516_5_.baseTranslationKey + ".criterion.failed", advancement.getId(), player.getName(), s);
                }

                notifyCommandListener(sender, this, p_193516_5_.baseTranslationKey + ".criterion.success", advancement.getId(), player.getName(), s);
            }
            else
            {
                if (args.length != 4)
                {
                    throw p_193516_6_.usage(p_193516_5_);
                }

                List<Advancement> list = this.getAdvancements(advancement, p_193516_6_);
                int i = p_193516_5_.perform(player, list);

                if (i == 0)
                {
                    throw p_193516_6_.fail(p_193516_5_, advancement.getId(), player.getName());
                }

                p_193516_6_.success(sender, this, p_193516_5_, advancement.getId(), player.getName(), i);
            }
        }
    }

    private void addChildren(Advancement p_193515_1_, List<Advancement> p_193515_2_)
    {
        for (Advancement advancement : p_193515_1_.getChildren())
        {
            p_193515_2_.add(advancement);
            this.addChildren(advancement, p_193515_2_);
        }
    }

    private List<Advancement> getAdvancements(Advancement p_193514_1_, Mode p_193514_2_)
    {
        List<Advancement> list = Lists.newArrayList();

        if (p_193514_2_.parents)
        {
            for (Advancement advancement = p_193514_1_.getParent(); advancement != null; advancement = advancement.getParent())
            {
                list.add(advancement);
            }
        }

        list.add(p_193514_1_);

        if (p_193514_2_.children)
        {
            this.addChildren(p_193514_1_, list);
        }

        return list;
    }

    private void testCriterion(ICommandSender p_192554_1_, EntityPlayerMP p_192554_2_, Advancement p_192554_3_, String p_192554_4_) throws CommandException
    {
        PlayerAdvancements playeradvancements = p_192554_2_.getAdvancements();
        CriterionProgress criterionprogress = playeradvancements.getProgress(p_192554_3_).getCriterionProgress(p_192554_4_);

        if (criterionprogress == null)
        {
            throw new CommandException("commands.advancement.criterionNotFound", p_192554_3_.getId(), p_192554_4_);
        }
        else if (!criterionprogress.isObtained())
        {
            throw new CommandException("commands.advancement.test.criterion.notDone", p_192554_2_.getName(), p_192554_3_.getId(), p_192554_4_);
        }
        else
        {
            notifyCommandListener(p_192554_1_, this, "commands.advancement.test.criterion.success", p_192554_2_.getName(), p_192554_3_.getId(), p_192554_4_);
        }
    }

    private void testAdvancement(ICommandSender p_192552_1_, EntityPlayerMP p_192552_2_, Advancement p_192552_3_) throws CommandException
    {
        AdvancementProgress advancementprogress = p_192552_2_.getAdvancements().getProgress(p_192552_3_);

        if (!advancementprogress.isDone())
        {
            throw new CommandException("commands.advancement.test.advancement.notDone", p_192552_2_.getName(), p_192552_3_.getId());
        }
        else
        {
            notifyCommandListener(p_192552_1_, this, "commands.advancement.test.advancement.success", p_192552_2_.getName(), p_192552_3_.getId());
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "grant", "revoke", "test");
        }
        else
        {
            ActionType advancementcommand$actiontype = ActionType.byName(args[0]);

            if (advancementcommand$actiontype != null)
            {
                if (args.length == 2)
                {
                    return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
                }

                if (args.length == 3)
                {
                    return getListOfStringsMatchingLastWord(args, Mode.NAMES);
                }

                Mode advancementcommand$mode = Mode.byName(args[2]);

                if (advancementcommand$mode != null && advancementcommand$mode != Mode.EVERYTHING)
                {
                    if (args.length == 4)
                    {
                        return getListOfStringsMatchingLastWord(args, this.getAdvancementNames(server));
                    }

                    if (args.length == 5 && advancementcommand$mode == Mode.ONLY)
                    {
                        Advancement advancement = server.getAdvancementManager().getAdvancement(new ResourceLocation(args[3]));

                        if (advancement != null)
                        {
                            return getListOfStringsMatchingLastWord(args, advancement.getCriteria().keySet());
                        }
                    }
                }
            }

            if ("test".equals(args[0]))
            {
                if (args.length == 2)
                {
                    return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
                }

                if (args.length == 3)
                {
                    return getListOfStringsMatchingLastWord(args, this.getAdvancementNames(server));
                }

                if (args.length == 4)
                {
                    Advancement advancement1 = server.getAdvancementManager().getAdvancement(new ResourceLocation(args[2]));

                    if (advancement1 != null)
                    {
                        return getListOfStringsMatchingLastWord(args, advancement1.getCriteria().keySet());
                    }
                }
            }

            return Collections.emptyList();
        }
    }

    private List<ResourceLocation> getAdvancementNames(MinecraftServer server)
    {
        List<ResourceLocation> list = Lists.newArrayList();

        for (Advancement advancement : server.getAdvancementManager().getAdvancements())
        {
            list.add(advancement.getId());
        }

        return list;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return args.length > 1 && ("grant".equals(args[0]) || "revoke".equals(args[0]) || "test".equals(args[0])) && index == 1;
    }

    public static Advancement findAdvancement(MinecraftServer server, String id) throws CommandException
    {
        Advancement advancement = server.getAdvancementManager().getAdvancement(new ResourceLocation(id));

        if (advancement == null)
        {
            throw new CommandException("commands.advancement.advancementNotFound", id);
        }
        else
        {
            return advancement;
        }
    }

    enum ActionType
    {
        GRANT("grant")
        {
            protected boolean perform(EntityPlayerMP p_193537_1_, Advancement p_193537_2_)
            {
                AdvancementProgress advancementprogress = p_193537_1_.getAdvancements().getProgress(p_193537_2_);

                if (advancementprogress.isDone())
                {
                    return false;
                }
                else
                {
                    for (String s : advancementprogress.getRemaningCriteria())
                    {
                        p_193537_1_.getAdvancements().grantCriterion(p_193537_2_, s);
                    }

                    return true;
                }
            }
            protected boolean performCriterion(EntityPlayerMP p_193535_1_, Advancement p_193535_2_, String p_193535_3_)
            {
                return p_193535_1_.getAdvancements().grantCriterion(p_193535_2_, p_193535_3_);
            }
        },
        REVOKE("revoke")
        {
            protected boolean perform(EntityPlayerMP p_193537_1_, Advancement p_193537_2_)
            {
                AdvancementProgress advancementprogress = p_193537_1_.getAdvancements().getProgress(p_193537_2_);

                if (!advancementprogress.hasProgress())
                {
                    return false;
                }
                else
                {
                    for (String s : advancementprogress.getCompletedCriteria())
                    {
                        p_193537_1_.getAdvancements().revokeCriterion(p_193537_2_, s);
                    }

                    return true;
                }
            }
            protected boolean performCriterion(EntityPlayerMP p_193535_1_, Advancement p_193535_2_, String p_193535_3_)
            {
                return p_193535_1_.getAdvancements().revokeCriterion(p_193535_2_, p_193535_3_);
            }
        };

        final String name;
        final String baseTranslationKey;

        ActionType(String nameIn)
        {
            this.name = nameIn;
            this.baseTranslationKey = "commands.advancement." + nameIn;
        }

        @Nullable
        static ActionType byName(String nameIn)
        {
            for (ActionType advancementcommand$actiontype : values())
            {
                if (advancementcommand$actiontype.name.equals(nameIn))
                {
                    return advancementcommand$actiontype;
                }
            }

            return null;
        }

        CommandException wrongUsage()
        {
            return new CommandException(this.baseTranslationKey + ".usage");
        }

        public int perform(EntityPlayerMP p_193532_1_, Iterable<Advancement> p_193532_2_)
        {
            int i = 0;

            for (Advancement advancement : p_193532_2_)
            {
                if (this.perform(p_193532_1_, advancement))
                {
                    ++i;
                }
            }

            return i;
        }

        protected abstract boolean perform(EntityPlayerMP p_193537_1_, Advancement p_193537_2_);

        protected abstract boolean performCriterion(EntityPlayerMP p_193535_1_, Advancement p_193535_2_, String p_193535_3_);
    }

    enum Mode
    {
        ONLY("only", false, false),
        THROUGH("through", true, true),
        FROM("from", false, true),
        UNTIL("until", true, false),
        EVERYTHING("everything", true, true);

        static final String[] NAMES = new String[values().length];
        final String name;
        final boolean parents;
        final boolean children;

        Mode(String p_i47556_3_, boolean p_i47556_4_, boolean p_i47556_5_)
        {
            this.name = p_i47556_3_;
            this.parents = p_i47556_4_;
            this.children = p_i47556_5_;
        }

        CommandException fail(ActionType p_193543_1_, Object... p_193543_2_)
        {
            return new CommandException(p_193543_1_.baseTranslationKey + "." + this.name + ".failed", p_193543_2_);
        }

        CommandException usage(ActionType p_193544_1_)
        {
            return new CommandException(p_193544_1_.baseTranslationKey + "." + this.name + ".usage");
        }

        void success(ICommandSender sender, AdvancementCommand p_193546_2_, ActionType type, Object... args)
        {
            CommandBase.notifyCommandListener(sender, p_193546_2_, type.baseTranslationKey + "." + this.name + ".success", args);
        }

        @Nullable
        static Mode byName(String nameIn)
        {
            for (Mode advancementcommand$mode : values())
            {
                if (advancementcommand$mode.name.equals(nameIn))
                {
                    return advancementcommand$mode;
                }
            }

            return null;
        }

        static {
            for (int i = 0; i < values().length; ++i)
            {
                NAMES[i] = values()[i].name;
            }
        }
    }
}
