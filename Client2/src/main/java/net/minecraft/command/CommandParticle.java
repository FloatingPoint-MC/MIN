package net.minecraft.command;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class CommandParticle extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getName()
    {
        return "particle";
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
        return "commands.particle.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 8)
        {
            throw new WrongUsageException("commands.particle.usage");
        }
        else
        {
            boolean flag = false;
            EnumParticleTypes enumparticletypes = EnumParticleTypes.getByName(args[0]);

            if (enumparticletypes == null)
            {
                throw new CommandException("commands.particle.notFound", args[0]);
            }
            else
            {
                String s = args[0];
                Vec3d vec3d = sender.getPositionVector();
                double d0 = (float)parseDouble(vec3d.x, args[1], true);
                double d1 = (float)parseDouble(vec3d.y, args[2], true);
                double d2 = (float)parseDouble(vec3d.z, args[3], true);
                double d3 = (float)parseDouble(args[4]);
                double d4 = (float)parseDouble(args[5]);
                double d5 = (float)parseDouble(args[6]);
                double d6 = (float)parseDouble(args[7]);
                int i = 0;

                if (args.length > 8)
                {
                    i = parseInt(args[8], 0);
                }

                boolean flag1 = args.length > 9 && "force".equals(args[9]);

                EntityPlayerMP entityplayermp;

                if (args.length > 10)
                {
                    entityplayermp = getPlayer(server, sender, args[10]);
                }
                else
                {
                    entityplayermp = null;
                }

                int[] aint = new int[enumparticletypes.getArgumentCount()];

                for (int j = 0; j < aint.length; ++j)
                {
                    if (args.length > 11 + j)
                    {
                        try
                        {
                            aint[j] = Integer.parseInt(args[11 + j]);
                        }
                        catch (NumberFormatException var28)
                        {
                            throw new CommandException("commands.particle.invalidParam", args[11 + j]);
                        }
                    }
                }

                World world = sender.getEntityWorld();

                if (world instanceof WorldServer)
                {
                    WorldServer worldserver = (WorldServer)world;

                    if (entityplayermp == null)
                    {
                        worldserver.spawnParticle(enumparticletypes, flag1, d0, d1, d2, i, d3, d4, d5, d6, aint);
                    }
                    else
                    {
                        worldserver.spawnParticle(entityplayermp, enumparticletypes, flag1, d0, d1, d2, i, d3, d4, d5, d6, aint);
                    }

                    notifyCommandListener(sender, this, "commands.particle.success", s, Math.max(i, 1));
                }
            }
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, EnumParticleTypes.getParticleNames());
        }
        else if (args.length > 1 && args.length <= 4)
        {
            return getTabCompletionCoordinate(args, 1, targetPos);
        }
        else if (args.length == 10)
        {
            return getListOfStringsMatchingLastWord(args, "normal", "force");
        }
        else
        {
            return args.length == 11 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 10;
    }
}
