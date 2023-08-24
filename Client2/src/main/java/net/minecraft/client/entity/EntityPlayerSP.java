package net.minecraft.client.entity;

import javax.annotation.Nullable;

import cn.floatingpoint.min.system.command.CommandMin;
import cn.floatingpoint.min.system.module.impl.render.impl.Particles;
import cn.floatingpoint.min.utils.client.ChatUtil;
import cn.floatingpoint.min.utils.math.TimeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ElytraSound;
import net.minecraft.client.audio.MovingSoundMinecartRiding;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditCommandBlockMinecart;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiEditStructure;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketClientStatus;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketRecipeInfo;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovementInput;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class EntityPlayerSP extends AbstractClientPlayer {
    public final NetHandlerPlayClient connection;
    private final StatisticsManager statWriter;
    private final RecipeBook recipeBook;
    private int permissionLevel = 0;

    /**
     * The last X position which was transmitted to the server, used to determine when the X position changes and needs
     * to be re-trasmitted
     */
    private double lastReportedPosX;

    /**
     * The last Y position which was transmitted to the server, used to determine when the Y position changes and needs
     * to be re-transmitted
     */
    private double lastReportedPosY;

    /**
     * The last Z position which was transmitted to the server, used to determine when the Z position changes and needs
     * to be re-transmitted
     */
    private double lastReportedPosZ;

    /**
     * The last yaw value which was transmitted to the server, used to determine when the yaw changes and needs to be
     * re-transmitted
     */
    private float lastReportedYaw;

    /**
     * The last pitch value which was transmitted to the server, used to determine when the pitch changes and needs to
     * be re-transmitted
     */
    private float lastReportedPitch;
    private boolean prevOnGround;

    /**
     * the last sneaking state sent to the server
     */
    private boolean serverSneakState;

    /**
     * the last sprinting state sent to the server
     */
    private boolean serverSprintState;

    /**
     * Reset to 0 every time position is sent to the server, used to send periodic updates every 20 ticks even when the
     * player is not moving.
     */
    private int positionUpdateTicks;
    private boolean hasValidHealth;
    private String serverBrand;
    public MovementInput movementInput;
    protected Minecraft mc;

    /**
     * Used to tell if the player pressed forward twice. If this is at 0 and it's pressed (And they are allowed to
     * sprint, aka enough food on the ground etc) it sets this to 7. If it's pressed and it's greater than 0 enable
     * sprinting.
     */
    protected int sprintToggleTimer;

    /**
     * Ticks left before sprinting is disabled.
     */
    public int sprintingTicksLeft;
    public float renderArmYaw;
    public float renderArmPitch;
    public float prevRenderArmYaw;
    public float prevRenderArmPitch;
    private int horseJumpPowerCounter;
    private float horseJumpPower;

    /**
     * The amount of time an entity has been in a Portal
     */
    public float timeInPortal;

    /**
     * The amount of time an entity has been in a Portal the previous tick
     */
    public float prevTimeInPortal;
    private boolean handActive;
    private EnumHand activeHand;
    private boolean rowingBoat;
    private int autoJumpTime;
    private boolean wasFallFlying;
    private final TimeHelper timer = new TimeHelper();
    private final String[] messages = new String[]{"党啊!你伟大的功绩将雕刻在历史的丰碑上，将永远留在我的心中！", "改革创造大奇迹，中华文明留记忆;发展才是硬道理，祖国翻天又覆地。", "一百年年风雨兼程，一百年年岁月沧桑，如今，强大的中国已经屹立在世界的东方。", "未来是美好的，党的方针政策是英明的，我们要永远拥护中国共产党，永远跟\n\247b\247l党走。永远！", "一个锤子，一把镰刀交织一起，看上去是一个普普通通的图案，可它贴到鲜红\n\247b\247l的旗帜上，它就是代表我们国家的一个政党：中国共产党。", "人生再大的困难也不比红军过草地艰难，生活再大的坎儿也不比红军过雪\n\247b\247l山艰辛，只要有勇气和信心，一切困难都是纸老虎。祝建党节快乐！", "英雄的中国共产党是中国人民的领导的核心，是领导中国人民从一个胜利\n\247b\247l走向另一个胜利的掌舵者，是指引中国走向繁荣昌盛富强明主的航灯！", "中国共产党，你是大树。你在风雨中展现自己的魅力，保护脚下的小草，让生\n\247b\247l命得以延续，让小草免受风雨的摧残，酷日的侵袭，严寒的凌辱。", "历史车轮滚滚向前，无情碾碎旧的封建的主义，先进的中国共产党应运而生，\n\247b\247l她带领中国走过几十年的风风雨雨，领导我们越过一个个坎坎坷坷。", "我坚信，我们就是沃土里的一颗种子，阳光下的一朵葵花，在党的关怀下，\n\247b\247l在和煦的阳光下，我们将会茁壮成长，把祖国的明天建设得更加富强。", "我们的伟大领袖毛主席曾经说过：“世界是你们的，也是我们的，但是归根\n\247b\247l结底是你们的。你们好像早晨八九点钟的太阳，希望寄托在你们身上。“", "歌颂共产党，没有共产党，就没有新中国!拌颂中国共产党，没有共产党，就没\n\247b\247l有人民当家做主!拌颂共产党，没有共产党，就没有我们的幸福生活。", "走进七月，所有的花都在绽放中吐露芬芳，所有的心都在祝福歌唱。党哺育\n\247b\247l我们快乐成长，党教导我们天天向上，我们一定不忘党的培养，为国贡献更多力量！", "确定今天的方针，设置明天的路线，存储坚定的信念，粘贴无悔的誓言，复制\n\247b\247l优秀的楷模，打印高尚的境界，发送深切的祝愿!祝党永远年轻，祝你永远快乐！", "革命风云变，夜沉沉，刀光剑影，奸徒背叛。革命航船何处去，党把乾坤扭转\n\247b\247l。听八一，南昌天半，一片枪声惊广宇。看城头，风卷红旗遍。喜此日，我军建。", "伟大的国家伟大的党，红日东升照四方，伟大的人民顶天立地，伟大的军队握\n\247b\247l紧枪，伟大的领袖毛泽东，领导我们向前，大海不能拦，高山不能挡，高山不能挡！", "从建立至今，中国共产党历经了无数的风雨，是我们最最敬爱的党员先烈们\n\247b\247l用自己的血肉身躯保家卫国，捍卫了中华大地不败的魄气，换来了今日的绚\n\247b\247l丽七彩虹！", "天府之国，万里长江肥沃良土，孕育佳品芳香飘逸，流连忘返境由心生，自在娇子\n\247b\247l。中国娇子，川渝骄傲阳光助学，兼济天下党的关怀，给予力量厚积薄发，再创辉煌。", "伟大的国家伟大的党，革命的旗帜高高飘扬，伟大的人民不怕风浪，伟大的军\n\247b\247l队步伐坚强，伟大的领袖毛泽东，领导我们向前，祖国要富强，人类要解放，人类要解放！", "在党的领导下，中华人民共和国成立了，社会主义实现了，改革开放成功了，\n\247b\247l香港、澳门回归了。我们的祖国已接脱了过去的屈辱和贫穷，发展成为较\n\247b\247l为繁荣昌盛的新中国。", "中国共产党，你是蜡烛。你不停地燃烧自己，你用自己的微光去照亮他人，却\n\247b\247l无遗憾。你用这光引领着无数人民去探索未知领域，为人民开辟了一条有\n\247b\247l中国特色的社会主义道路。", "一唱雄鸡天下白，唤来春天照人间。从此，我们的祖国进入了建设社会主义\n\247b\247l的新时代。国民经济和各项事业取得了举世瞩目的巨大成就，一个充满生\n\247b\247l机活力的中国崛起在世界的东方。", "五颗金星映党旗，党的光辉耀寰宇，一百载书华章，民族复兴展伟业，国人共\n\247b\247l圆中国梦。百年风雨育娇子，天之娇子世传承，龙飞凤舞成吉祥，激情奋进续\n\247b\247l传奇，全员齐筑娇子梦。", "历史的接力棒传入我们新一代人手中，历史给了我们足够的选择，过平平坦\n\247b\247l坦的生活。我们应把这交付于我们手中的祖国带向更昌盛富强的时代，让\n\247b\247l我们用生命，来创造历史的另一个辉煌！", "中国共产党，你是玉兰。你纯洁高雅，弃妖冶之色，去轻佻之态。你不选择在\n\247b\247l温暖舒适的暮春中吐艳，却在冷雨中挺立，在寒风中怒放。你无论高缀枝头，还是\n\247b\247l飘落在地，始终保持着一尘不染的品格。", "每个人的信仰是不同的。但是，信仰一个政党，加入一个先进的组织，这是人\n\247b\247l生最高尚的追求。中国共产党之所以值得信仰是因为她有光辉的历史，是\n\247b\247l中华民族的希望所在，也是我们炎黄子孙的骄傲。", "悠悠一百载，走过了多少坎坷与荆棘，品尝了多少屈辱和血泪，更记载了多少\n\247b\247l沧海变桑田的伟大业绩！", "中国共产党，中国拥有你，有了一个光明的前程，我们拥有你，便有了一个\n\247b\247l不舍的信念、不弃的追求。"};
    private int tick = 0;

    public EntityPlayerSP(Minecraft p_i47378_1_, World p_i47378_2_, NetHandlerPlayClient p_i47378_3_, StatisticsManager p_i47378_4_, RecipeBook p_i47378_5_) {
        super(p_i47378_2_, p_i47378_3_.getGameProfile());
        this.connection = p_i47378_3_;
        this.statWriter = p_i47378_4_;
        this.recipeBook = p_i47378_5_;
        this.mc = p_i47378_1_;
        this.dimension = 0;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(float healAmount) {
    }

    public boolean startRiding(Entity entityIn, boolean force) {
        if (!super.startRiding(entityIn, force)) {
            return false;
        } else {
            if (entityIn instanceof EntityMinecart) {
                this.mc.getSoundHandler().playSound(new MovingSoundMinecartRiding(this, (EntityMinecart) entityIn));
            }

            if (entityIn instanceof EntityBoat) {
                this.prevRotationYaw = entityIn.rotationYaw;
                this.rotationYaw = entityIn.rotationYaw;
                this.setRotationYawHead(entityIn.rotationYaw);
            }

            return true;
        }
    }

    /**
     * Dismounts this entity from the entity it is riding.
     */
    public void dismountRidingEntity() {
        super.dismountRidingEntity();
        this.rowingBoat = false;
    }

    /**
     * interpolated look vector
     */
    public Vec3d getLook(float partialTicks) {
        return this.getVectorForRotation(this.rotationPitch, this.rotationYaw);
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        if (this.world.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ))) {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd");
            String date = format.format(new Date());
            if (date.equals("7-1") || date.equals("07-1") || date.equals("7-01") || date.equals("07-01")) {
                if (this.timer.isDelayComplete(60000L)) {
                    this.timer.reset();
                    ChatUtil.printToChat(new TextComponentString("\247b\247l" + this.messages[this.tick]));
                    this.tick++;
                    if (this.tick >= this.messages.length) {
                        this.tick = 0;
                    }
                }
            }
            super.onUpdate();

            if (this.isRiding()) {
                this.connection.sendPacket(new CPacketPlayer.Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
                this.connection.sendPacket(new CPacketInput(this.moveStrafing, this.moveForward, this.movementInput.jump, this.movementInput.sneak));
                Entity entity = this.getLowestRidingEntity();

                if (entity != this && entity.canPassengerSteer()) {
                    this.connection.sendPacket(new CPacketVehicleMove(entity));
                }
            } else {
                this.onUpdateWalkingPlayer();
            }
        }
    }

    /**
     * called every tick when the player is on foot. Performs all the things that normally happen during movement.
     */
    private void onUpdateWalkingPlayer() {
        boolean flag = this.isSprinting();

        if (flag != this.serverSprintState) {
            if (flag) {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SPRINTING));
            } else {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SPRINTING));
            }

            this.serverSprintState = flag;
        }

        boolean flag1 = this.isSneaking();

        if (flag1 != this.serverSneakState) {
            if (flag1) {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_SNEAKING));
            } else {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.STOP_SNEAKING));
            }

            this.serverSneakState = flag1;
        }

        if (this.isCurrentViewEntity()) {
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            double d0 = this.posX - this.lastReportedPosX;
            double d1 = axisalignedbb.minY - this.lastReportedPosY;
            double d2 = this.posZ - this.lastReportedPosZ;
            double d3 = this.rotationYaw - this.lastReportedYaw;
            double d4 = this.rotationPitch - this.lastReportedPitch;
            ++this.positionUpdateTicks;
            boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D || this.positionUpdateTicks >= 20;
            boolean flag3 = d3 != 0.0D || d4 != 0.0D;

            if (this.isRiding()) {
                this.connection.sendPacket(new CPacketPlayer.PositionRotation(this.motionX, -999.0D, this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
                flag2 = false;
            } else if (flag2 && flag3) {
                this.connection.sendPacket(new CPacketPlayer.PositionRotation(this.posX, axisalignedbb.minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
            } else if (flag2) {
                this.connection.sendPacket(new CPacketPlayer.Position(this.posX, axisalignedbb.minY, this.posZ, this.onGround));
            } else if (flag3) {
                this.connection.sendPacket(new CPacketPlayer.Rotation(this.rotationYaw, this.rotationPitch, this.onGround));
            } else if (this.prevOnGround != this.onGround) {
                this.connection.sendPacket(new CPacketPlayer(this.onGround));
            }

            if (flag2) {
                this.lastReportedPosX = this.posX;
                this.lastReportedPosY = axisalignedbb.minY;
                this.lastReportedPosZ = this.posZ;
                this.positionUpdateTicks = 0;
            }

            if (flag3) {
                this.lastReportedYaw = this.rotationYaw;
                this.lastReportedPitch = this.rotationPitch;
            }

            this.prevOnGround = this.onGround;
        }
    }


    /**
     * Drop one item out of the currently selected stack if {@code dropAll} is false. If {@code dropItem} is true the
     * entire stack is dropped.
     */
    @Nullable
    public EntityItem dropItem(boolean dropAll) {
        CPacketPlayerDigging.Action cpacketplayerdigging$action = dropAll ? CPacketPlayerDigging.Action.DROP_ALL_ITEMS : CPacketPlayerDigging.Action.DROP_ITEM;
        this.connection.sendPacket(new CPacketPlayerDigging(cpacketplayerdigging$action, BlockPos.ORIGIN, EnumFacing.DOWN));
        return null;
    }

    protected ItemStack dropItemAndGetStack(EntityItem p_184816_1_) {
        return ItemStack.EMPTY;
    }

    /**
     * Sends a chat message from the player.
     */
    public void sendChatMessage(String message) {
        if (message.toLowerCase().startsWith("/min")) {
            if (CommandMin.execute(message.substring(5).split(" "))) {
                return;
            }
        }
        this.connection.sendPacket(new CPacketChatMessage(message));
    }

    public void swingArm(EnumHand hand, boolean sendPacket) {
        super.swingArm(hand);
        if (sendPacket) {
            this.connection.sendPacket(new CPacketAnimation(hand));
        }
    }

    public void respawnPlayer() {
        this.connection.sendPacket(new CPacketClientStatus(CPacketClientStatus.State.PERFORM_RESPAWN));
    }

    /**
     * Deals damage to the entity. This will take the armor of the entity into consideration before damaging the health
     * bar.
     */
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc)) {
            this.setHealth(this.getHealth() - damageAmount);
        }
    }

    /**
     * set current crafting inventory back to the 2x2 square
     */
    public void closeScreen() {
        this.connection.sendPacket(new CPacketCloseWindow(this.openContainer.windowId));
        this.closeScreenAndDropStack();
    }

    public void closeScreenAndDropStack() {
        this.inventory.setItemStack(ItemStack.EMPTY);
        super.closeScreen();
        this.mc.displayGuiScreen(null);
    }

    /**
     * Updates health locally.
     */
    public void setPlayerSPHealth(float health) {
        if (this.hasValidHealth) {
            float f = this.getHealth() - health;

            if (f <= 0.0F) {
                this.setHealth(health);

                if (f < 0.0F) {
                    this.hurtResistantTime = this.maxHurtResistantTime / 2;
                }
            } else {
                this.lastDamage = f;
                this.setHealth(this.getHealth());
                this.hurtResistantTime = this.maxHurtResistantTime;
                this.damageEntity(DamageSource.GENERIC, f);
                this.maxHurtTime = 10;
                this.hurtTime = this.maxHurtTime;
            }
        } else {
            this.setHealth(health);
            this.hasValidHealth = true;
        }
    }

    /**
     * Adds a value to a statistic field.
     */
    public void addStat(@Nullable StatBase stat, int amount) {
        if (stat != null) {
            if (stat.isIndependent) {
                super.addStat(stat, amount);
            }
        }
    }

    /**
     * Sends the player's abilities to the server (if there is one).
     */
    public void sendPlayerAbilities() {
        this.connection.sendPacket(new CPacketPlayerAbilities(this.capabilities));
    }

    /**
     * returns true if this is an EntityPlayerSP, or the logged in player.
     */
    public boolean isUser() {
        return true;
    }

    protected void sendHorseJump() {
        this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_RIDING_JUMP, MathHelper.floor(this.getHorseJumpPower() * 100.0F)));
    }

    public void sendHorseInventory() {
        this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.OPEN_INVENTORY));
    }

    /**
     * Sets the brand of the currently connected server. Server brand information is sent over the {@code MC|Brand}
     * plugin channel, and is used to identify modded servers in crash reports.
     */
    public void setServerBrand(String brand) {
        this.serverBrand = brand;
    }

    /**
     * Gets the brand of the currently connected server. May be null if the server hasn't yet sent brand information.
     * Server brand information is sent over the {@code MC|Brand} plugin channel, and is used to identify modded servers
     * in crash reports.
     */
    public String getServerBrand() {
        return this.serverBrand;
    }

    public StatisticsManager getStatFileWriter() {
        return this.statWriter;
    }

    public RecipeBook getRecipeBook() {
        return this.recipeBook;
    }

    public void removeRecipeHighlight(IRecipe p_193103_1_) {
        if (this.recipeBook.isNew(p_193103_1_)) {
            this.recipeBook.markSeen(p_193103_1_);
            this.connection.sendPacket(new CPacketRecipeInfo(p_193103_1_));
        }
    }

    public int getPermissionLevel() {
        return this.permissionLevel;
    }

    public void setPermissionLevel(int p_184839_1_) {
        this.permissionLevel = p_184839_1_;
    }

    public void sendStatusMessage(ITextComponent chatComponent, boolean actionBar) {
        if (actionBar) {
            this.mc.ingameGUI.setOverlayMessage(chatComponent, false);
        } else {
            this.mc.ingameGUI.getChatGUI().printChatMessage(chatComponent);
        }
    }

    protected boolean pushOutOfBlocks(double x, double y, double z) {
        if (!this.noClip) {
            BlockPos blockpos = new BlockPos(x, y, z);
            double d0 = x - (double) blockpos.getX();
            double d1 = z - (double) blockpos.getZ();

            if (!this.isOpenBlockSpace(blockpos)) {
                int i = -1;
                double d2 = 9999.0D;

                if (this.isOpenBlockSpace(blockpos.west()) && d0 < d2) {
                    d2 = d0;
                    i = 0;
                }

                if (this.isOpenBlockSpace(blockpos.east()) && 1.0D - d0 < d2) {
                    d2 = 1.0D - d0;
                    i = 1;
                }

                if (this.isOpenBlockSpace(blockpos.north()) && d1 < d2) {
                    d2 = d1;
                    i = 4;
                }

                if (this.isOpenBlockSpace(blockpos.south()) && 1.0D - d1 < d2) {
                    i = 5;
                }

                if (i == 0) {
                    this.motionX = -0.10000000149011612D;
                }

                if (i == 1) {
                    this.motionX = 0.10000000149011612D;
                }

                if (i == 4) {
                    this.motionZ = -0.10000000149011612D;
                }

                if (i == 5) {
                    this.motionZ = 0.10000000149011612D;
                }
            }

        }
        return false;
    }

    /**
     * Returns true if the block at the given BlockPos and the block above it are NOT full cubes.
     */
    private boolean isOpenBlockSpace(BlockPos pos) {
        return !this.world.getBlockState(pos).isNormalCube() && !this.world.getBlockState(pos.up()).isNormalCube();
    }

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean sprinting) {
        super.setSprinting(sprinting);
        this.sprintingTicksLeft = 0;
    }

    /**
     * Sets the current XP, total XP, and level number.
     */
    public void setXPStats(float currentXP, int maxXP, int level) {
        this.experience = currentXP;
        this.experienceTotal = maxXP;
        this.experienceLevel = level;
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void sendMessage(ITextComponent component) {
        this.mc.ingameGUI.getChatGUI().printChatMessage(component);
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canUseCommand(int permLevel, String commandName) {
        return permLevel <= this.getPermissionLevel();
    }

    /**
     * Handler for {@link World#setEntityState}
     */
    public void handleStatusUpdate(byte id) {
        if (id >= 24 && id <= 28) {
            this.setPermissionLevel(id - 24);
        } else {
            super.handleStatusUpdate(id);
        }
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition() {
        return new BlockPos(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D);
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        this.world.playSound(this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch, false);
    }

    /**
     * Returns whether the entity is in a server world
     */
    public boolean isServerWorld() {
        return true;
    }

    public void setActiveHand(EnumHand hand) {
        ItemStack itemstack = this.getHeldItem(hand);

        if (!itemstack.isEmpty() && !this.isHandActive()) {
            super.setActiveHand(hand);
            this.handActive = true;
            this.activeHand = hand;
        }
    }

    public boolean isHandActive() {
        return this.handActive;
    }

    public void resetActiveHand() {
        super.resetActiveHand();
        this.handActive = false;
    }

    public EnumHand getActiveHand() {
        return this.activeHand;
    }

    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);

        if (HAND_STATES.equals(key)) {
            boolean flag = (this.dataManager.get(HAND_STATES) & 1) > 0;
            EnumHand enumhand = (this.dataManager.get(HAND_STATES) & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;

            if (flag && !this.handActive) {
                this.setActiveHand(enumhand);
            } else if (!flag && this.handActive) {
                this.resetActiveHand();
            }
        }

        if (FLAGS.equals(key) && this.isElytraFlying() && !this.wasFallFlying) {
            this.mc.getSoundHandler().playSound(new ElytraSound(this));
        }
    }

    public boolean isRidingHorse() {
        Entity entity = this.getRidingEntity();
        return this.isRiding() && entity instanceof IJumpingMount && ((IJumpingMount) entity).canJump();
    }

    public float getHorseJumpPower() {
        return this.horseJumpPower;
    }

    public void openEditSign(TileEntitySign signTile) {
        this.mc.displayGuiScreen(new GuiEditSign(signTile));
    }

    public void displayGuiEditCommandCart(CommandBlockBaseLogic commandBlock) {
        this.mc.displayGuiScreen(new GuiEditCommandBlockMinecart(commandBlock));
    }

    public void displayGuiCommandBlock(TileEntityCommandBlock commandBlock) {
        this.mc.displayGuiScreen(new GuiCommandBlock(commandBlock));
    }

    public void openEditStructure(TileEntityStructure structure) {
        this.mc.displayGuiScreen(new GuiEditStructure(structure));
    }

    public void openBook(ItemStack stack, EnumHand hand) {
        Item item = stack.getItem();

        if (item == Items.WRITABLE_BOOK) {
            this.mc.displayGuiScreen(new GuiScreenBook(this, stack, true));
        }
    }

    /**
     * Displays the GUI for interacting with a chest inventory.
     */
    public void displayGUIChest(IInventory chestInventory) {
        String s = chestInventory instanceof IInteractionObject ? ((IInteractionObject) chestInventory).getGuiID() : "minecraft:container";

        if ("minecraft:chest".equals(s)) {
            this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
        } else if ("minecraft:hopper".equals(s)) {
            this.mc.displayGuiScreen(new GuiHopper(this.inventory, chestInventory));
        } else if ("minecraft:furnace".equals(s)) {
            this.mc.displayGuiScreen(new GuiFurnace(this.inventory, chestInventory));
        } else if ("minecraft:brewing_stand".equals(s)) {
            this.mc.displayGuiScreen(new GuiBrewingStand(this.inventory, chestInventory));
        } else if ("minecraft:beacon".equals(s)) {
            this.mc.displayGuiScreen(new GuiBeacon(this.inventory, chestInventory));
        } else if (!"minecraft:dispenser".equals(s) && !"minecraft:dropper".equals(s)) {
            if ("minecraft:shulker_box".equals(s)) {
                this.mc.displayGuiScreen(new GuiShulkerBox(this.inventory, chestInventory));
            } else {
                this.mc.displayGuiScreen(new GuiChest(this.inventory, chestInventory));
            }
        } else {
            this.mc.displayGuiScreen(new GuiDispenser(this.inventory, chestInventory));
        }
    }

    public void openGuiHorseInventory(AbstractHorse horse, IInventory inventoryIn) {
        this.mc.displayGuiScreen(new GuiScreenHorseInventory(this.inventory, inventoryIn, horse));
    }

    public void displayGui(IInteractionObject guiOwner) {
        String s = guiOwner.getGuiID();

        switch (s) {
            case "minecraft:crafting_table":
                this.mc.displayGuiScreen(new GuiCrafting(this.inventory, this.world));
                break;
            case "minecraft:enchanting_table":
                this.mc.displayGuiScreen(new GuiEnchantment(this.inventory, this.world, guiOwner));
                break;
            case "minecraft:anvil":
                this.mc.displayGuiScreen(new GuiRepair(this.inventory, this.world));
                break;
        }
    }

    public void displayVillagerTradeGui(IMerchant villager) {
        this.mc.displayGuiScreen(new GuiMerchant(this.inventory, villager, this.world));
    }

    /**
     * Called when the entity is dealt a critical hit.
     */
    public void onCriticalHit(Entity entityHit) {
        if (Particles.crit.getValue()) {
            for (int i = 0; i < Particles.critAmplifier.getValue(); i++) {
                this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT);
            }
        }
    }

    public void onEnchantmentCritical(Entity entityHit) {
        if (Particles.sharpness.getValue()) {
            for (int i = 0; i < Particles.sharpnessAmplifier.getValue(); i++) {
                this.mc.effectRenderer.emitParticleAtEntity(entityHit, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }

    /**
     * Returns if this entity is sneaking.
     */
    public boolean isSneaking() {
        boolean flag = this.movementInput != null && this.movementInput.sneak;
        return flag && !this.sleeping;
    }

    public void updateEntityActionState() {
        super.updateEntityActionState();

        if (this.isCurrentViewEntity()) {
            this.moveStrafing = this.movementInput.moveStrafe;
            this.moveForward = this.movementInput.moveForward;
            this.isJumping = this.movementInput.jump;
            this.prevRenderArmYaw = this.renderArmYaw;
            this.prevRenderArmPitch = this.renderArmPitch;
            this.renderArmPitch = (float) ((double) this.renderArmPitch + (double) (this.rotationPitch - this.renderArmPitch) * 0.5D);
            this.renderArmYaw = (float) ((double) this.renderArmYaw + (double) (this.rotationYaw - this.renderArmYaw) * 0.5D);
        }
    }

    protected boolean isCurrentViewEntity() {
        return this.mc.getRenderViewEntity() == this;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        ++this.sprintingTicksLeft;

        if (this.sprintToggleTimer > 0) {
            --this.sprintToggleTimer;
        }

        this.prevTimeInPortal = this.timeInPortal;

        if (this.inPortal) {
            if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
                if (this.mc.currentScreen instanceof GuiContainer) {
                    this.closeScreen();
                }

                this.mc.displayGuiScreen(null);
            }

            if (this.timeInPortal == 0.0F) {
                this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.BLOCK_PORTAL_TRIGGER, this.rand.nextFloat() * 0.4F + 0.8F));
            }

            this.timeInPortal += 0.0125F;

            if (this.timeInPortal >= 1.0F) {
                this.timeInPortal = 1.0F;
            }

            this.inPortal = false;
        } else if (this.isPotionActive(MobEffects.NAUSEA) && Objects.requireNonNull(this.getActivePotionEffect(MobEffects.NAUSEA)).getDuration() > 60) {
            this.timeInPortal += 0.006666667F;

            if (this.timeInPortal > 1.0F) {
                this.timeInPortal = 1.0F;
            }
        } else {
            if (this.timeInPortal > 0.0F) {
                this.timeInPortal -= 0.05F;
            }

            if (this.timeInPortal < 0.0F) {
                this.timeInPortal = 0.0F;
            }
        }

        if (this.timeUntilPortal > 0) {
            --this.timeUntilPortal;
        }

        boolean flag = this.movementInput.jump;
        boolean flag1 = this.movementInput.sneak;
        boolean flag2 = this.movementInput.moveForward >= 0.8F;
        this.movementInput.updatePlayerMoveState();

        if (this.isHandActive() && !this.isRiding()) {
            this.movementInput.moveStrafe *= 0.2F;
            this.movementInput.moveForward *= 0.2F;
            this.sprintToggleTimer = 0;
        }

        boolean flag3 = false;

        if (this.autoJumpTime > 0) {
            --this.autoJumpTime;
            flag3 = true;
            this.movementInput.jump = true;
        }

        AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
        this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, axisalignedbb.minY + 0.5D, this.posZ + (double) this.width * 0.35D);
        this.pushOutOfBlocks(this.posX - (double) this.width * 0.35D, axisalignedbb.minY + 0.5D, this.posZ - (double) this.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, axisalignedbb.minY + 0.5D, this.posZ - (double) this.width * 0.35D);
        this.pushOutOfBlocks(this.posX + (double) this.width * 0.35D, axisalignedbb.minY + 0.5D, this.posZ + (double) this.width * 0.35D);
        boolean flag4 = (float) this.getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying;

        if (this.onGround && !flag1 && !flag2 && this.movementInput.moveForward >= 0.8F && !this.isSprinting() && flag4 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS)) {
            if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
                this.sprintToggleTimer = 7;
            } else {
                this.setSprinting(true);
            }
        }

        if (!this.isSprinting() && this.movementInput.moveForward >= 0.8F && flag4 && !this.isHandActive() && !this.isPotionActive(MobEffects.BLINDNESS) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
            this.setSprinting(true);
        }

        if (this.isSprinting() && (this.movementInput.moveForward < 0.8F || this.collidedHorizontally || !flag4)) {
            this.setSprinting(false);
        }

        if (this.capabilities.allowFlying) {
            if (this.mc.playerController.isSpectatorMode()) {
                if (!this.capabilities.isFlying) {
                    this.capabilities.isFlying = true;
                    this.sendPlayerAbilities();
                }
            } else if (!flag && this.movementInput.jump && !flag3) {
                if (this.flyToggleTimer == 0) {
                    this.flyToggleTimer = 7;
                } else {
                    this.capabilities.isFlying = !this.capabilities.isFlying;
                    this.sendPlayerAbilities();
                    this.flyToggleTimer = 0;
                }
            }
        }

        if (this.movementInput.jump && !flag && !this.onGround && this.motionY < 0.0D && !this.isElytraFlying() && !this.capabilities.isFlying) {
            ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack)) {
                this.connection.sendPacket(new CPacketEntityAction(this, CPacketEntityAction.Action.START_FALL_FLYING));
            }
        }

        this.wasFallFlying = this.isElytraFlying();

        if (this.capabilities.isFlying && this.isCurrentViewEntity()) {
            if (this.movementInput.sneak) {
                this.movementInput.moveStrafe = (float) ((double) this.movementInput.moveStrafe / 0.3D);
                this.movementInput.moveForward = (float) ((double) this.movementInput.moveForward / 0.3D);
                this.motionY -= this.capabilities.getFlySpeed() * 3.0F;
            }

            if (this.movementInput.jump) {
                this.motionY += this.capabilities.getFlySpeed() * 3.0F;
            }
        }

        if (this.isRidingHorse()) {
            IJumpingMount ijumpingmount = (IJumpingMount) this.getRidingEntity();

            if (this.horseJumpPowerCounter < 0) {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter == 0) {
                    this.horseJumpPower = 0.0F;
                }
            }

            if (flag && !this.movementInput.jump) {
                this.horseJumpPowerCounter = -10;
                assert ijumpingmount != null;
                ijumpingmount.setJumpPower(MathHelper.floor(this.getHorseJumpPower() * 100.0F));
                this.sendHorseJump();
            } else if (!flag && this.movementInput.jump) {
                this.horseJumpPowerCounter = 0;
                this.horseJumpPower = 0.0F;
            } else if (flag) {
                ++this.horseJumpPowerCounter;

                if (this.horseJumpPowerCounter < 10) {
                    this.horseJumpPower = (float) this.horseJumpPowerCounter * 0.1F;
                } else {
                    this.horseJumpPower = 0.8F + 2.0F / (float) (this.horseJumpPowerCounter - 9) * 0.1F;
                }
            }
        } else {
            this.horseJumpPower = 0.0F;
        }

        super.onLivingUpdate();

        if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
            this.capabilities.isFlying = false;
            this.sendPlayerAbilities();
        }
    }

    /**
     * Handles updating while riding another entity
     */
    public void updateRidden() {
        super.updateRidden();
        this.rowingBoat = false;

        if (this.getRidingEntity() instanceof EntityBoat) {
            EntityBoat entityboat = (EntityBoat) this.getRidingEntity();
            entityboat.updateInputs(this.movementInput.leftKeyDown, this.movementInput.rightKeyDown, this.movementInput.forwardKeyDown, this.movementInput.backKeyDown);
            this.rowingBoat |= this.movementInput.leftKeyDown || this.movementInput.rightKeyDown || this.movementInput.forwardKeyDown || this.movementInput.backKeyDown;
        }
    }

    public boolean isRowingBoat() {
        return this.rowingBoat;
    }


    /**
     * Removes the given potion effect from the active potion map and returns it. Does not call cleanup callbacks for
     * the end of the potion effect.
     */
    @Nullable
    public PotionEffect removeActivePotionEffect(@Nullable Potion potioneffectin) {
        if (potioneffectin == MobEffects.NAUSEA) {
            this.prevTimeInPortal = 0.0F;
            this.timeInPortal = 0.0F;
        }

        return super.removeActivePotionEffect(potioneffectin);
    }
}
