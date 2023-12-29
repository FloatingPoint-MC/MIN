package net.minecraft.client;

import cn.floatingpoint.min.MIN;
import cn.floatingpoint.min.management.Managers;
import cn.floatingpoint.min.system.hyt.packet.impl.Hyt0Packet;
import cn.floatingpoint.min.system.hyt.world.HYTChunkExecutor;
import cn.floatingpoint.min.system.irc.IRCMessageGrabber;
import cn.floatingpoint.min.system.module.Module;
import cn.floatingpoint.min.system.module.impl.boost.BoostModule;
import cn.floatingpoint.min.system.module.impl.misc.MiscModule;
import cn.floatingpoint.min.system.module.impl.render.impl.Animation;
import cn.floatingpoint.min.system.module.impl.render.impl.KeyStrokes;
import cn.floatingpoint.min.system.module.impl.render.impl.Spinning;
import cn.floatingpoint.min.system.shortcut.Shortcut;
import cn.floatingpoint.min.system.ui.loading.GuiDamnJapaneseAction;
import cn.floatingpoint.min.system.ui.loading.GuiLoading;
import cn.floatingpoint.min.system.ui.mainmenu.DebugMainMenu;
import cn.floatingpoint.min.system.ui.mainmenu.MINMainMenu;
import cn.floatingpoint.min.utils.client.ChatUtil;
import cn.floatingpoint.min.utils.client.WebUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.common.hash.Hashing;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.AuthenticationService;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.main.GameConfiguration;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.util.ISearchTree;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.client.util.SearchTree;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Bootstrap;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.profiler.Profiler;
import net.minecraft.profiler.Snooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.*;
import net.minecraft.util.Timer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjglx.Sys;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;
import org.lwjglx.opengl.ContextCapabilities;
import org.lwjglx.opengl.Display;
import org.lwjglx.opengl.DisplayMode;
import org.lwjglx.opengl.GLContext;
import org.lwjglx.util.glu.GLU;

public class Minecraft implements IThreadListener, ISnooperInfo {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation LOCATION_MOJANG_PNG = new ResourceLocation("min/loading.png");
    public static final boolean IS_RUNNING_ON_MAC = Util.getOSType() == Util.EnumOS.OSX;

    /**
     * A 10MiB pre-allocation to ensure the heap is reasonably sized. {@linkplain #freeMemory() Freed} when the game runs
     * out of memory.
     *
     * @see #freeMemory()
     */
    public static byte[] memoryReserve = new byte[10485760];
    private static final List<DisplayMode> MAC_DISPLAY_MODES = Lists.newArrayList(new DisplayMode(2560, 1600), new DisplayMode(2880, 1800));
    private final File fileResourcepacks;
    /**
     * The player's GameProfile properties
     */
    private final PropertyMap profileProperties;
    private ServerData currentServerData;

    /**
     * The RenderEngine instance used by Minecraft
     */
    private TextureManager renderEngine;

    /**
     * The instance of the Minecraft Client, set in the constructor.
     */
    private static Minecraft instance;
    private final DataFixer dataFixer;
    public PlayerControllerMP playerController;
    private boolean fullscreen;
    private boolean hasCrashed;

    /**
     * Instance of CrashReport.
     */
    private CrashReport crashReporter;
    public int displayWidth;
    public int displayHeight;
    private final Timer timer = new Timer(20.0F);

    /**
     * Instance of PlayerUsageSnooper.
     */
    private final Snooper usageSnooper = new Snooper("client", this, MinecraftServer.getCurrentTimeMillis());
    public WorldClient world;
    public RenderGlobal renderGlobal;
    private RenderManager renderManager;
    private RenderItem renderItem;
    private ItemRenderer itemRenderer;
    public EntityPlayerSP player;
    @Nullable
    private Entity renderViewEntity;
    public Entity pointedEntity;
    public ParticleManager effectRenderer;

    /**
     * Manages all search trees
     */
    private final SearchTreeManager searchTreeManager = new SearchTreeManager();
    private final Session session;
    private boolean isGamePaused;

    /**
     * Time passed since the last update in ticks. Used instead of this.timer.renderPartialTicks when paused in
     * singleplayer.
     */
    private float renderPartialTicksPaused;

    /**
     * The font renderer used for displaying and measuring text
     */
    public FontRenderer fontRenderer;
    public FontRenderer standardGalacticFontRenderer;

    /**
     * The GuiScreen that's being displayed at the moment.
     */
    @Nullable
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;
    public DebugRenderer debugRenderer;

    /**
     * Mouse left click counter
     */
    private int leftClickCounter;

    /**
     * Display width
     */
    private final int tempDisplayWidth;

    /**
     * Display height
     */
    private final int tempDisplayHeight;

    /**
     * Instance of IntegratedServer.
     */
    @Nullable
    private IntegratedServer integratedServer;
    public GuiIngame ingameGUI;

    /**
     * Skip render world
     */
    public boolean skipRenderWorld;

    /**
     * The ray trace hit that the mouse is over.
     */
    public RayTraceResult objectMouseOver;

    /**
     * The game settings that currently hold effect.
     */
    public GameSettings gameSettings;
    public CreativeSettings creativeSettings;

    /**
     * Mouse helper instance.
     */
    public MouseHelper mouseHelper;
    public final File gameDir;
    private final File fileAssets;
    private final String launchedVersion;
    private final String versionType;
    private final Proxy proxy;
    private ISaveFormat saveLoader;

    /**
     * This is set to fpsCounter every debug screen update, and is shown on the debug screen. It's also sent as part of
     * the usage snooping.
     */
    private static int debugFPS;

    /**
     * When you place a block, it's set to 6, decremented once per tick, when it's 0, you can place another block.
     */
    private int rightClickDelayTimer;
    private String serverName;
    private int serverPort;

    /**
     * Does the actual gameplay have focus. If so then mouse and keys will affect the player instead of menus.
     */
    public boolean inGameHasFocus;
    long systemTime = getSystemTime();

    /**
     * Join player counter
     */
    private int joinPlayerCounter;

    /**
     * The FrameTimer's instance
     */
    public final FrameTimer frameTimer = new FrameTimer();

    /**
     * Time in nanoseconds of when the class is loaded
     */
    long startNanoTime = System.nanoTime();
    private final boolean jvm64bit;
    @Nullable
    private NetworkManager networkManager;
    private boolean integratedServerIsRunning;

    /**
     * The profiler instance
     */
    public final Profiler profiler = new Profiler();

    /**
     * Keeps track of how long the debug crash key combo (F3+C) has been pressed for, in order to crash after 10 seconds.
     */
    private long debugCrashKeyPressTime = -1L;
    private IReloadableResourceManager resourceManager;
    private final MetadataSerializer metadataSerializer = new MetadataSerializer();
    private final List<IResourcePack> defaultResourcePacks = Lists.newArrayList();
    private final DefaultResourcePack defaultResourcePack;
    private ResourcePackRepository resourcePackRepository;
    private LanguageManager languageManager;
    private BlockColors blockColors;
    private Framebuffer framebuffer;
    private TextureMap textureMapBlocks;
    private SoundHandler soundHandler;
    private MusicTicker musicTicker;
    private ResourceLocation mojangLogo;
    private final MinecraftSessionService sessionService;
    private SkinManager skinManager;
    private final Queue<FutureTask<?>> scheduledTasks = Queues.newArrayDeque();
    private final Thread thread = Thread.currentThread();

    /**
     * The BlockRenderDispatcher instance that will be used based off game settings
     */
    private BlockRendererDispatcher blockRenderDispatcher;
    private final GuiToast toastGui;

    /**
     * Set to true to keep the game loop running. Set to false by shutdown() to allow the game loop to exit cleanly.
     */
    public volatile boolean running = true;

    /**
     * String that shows the debug information
     */
    public String debug = "";
    public boolean renderChunksMany = true;

    /**
     * Approximate time (in ms) of last update to debug string
     */
    private long debugUpdateTime = getSystemTime();

    /**
     * holds the current fps
     */
    private int fpsCounter;
    public boolean actionKeyF3;
    long prevFrameTime = -1L;

    /**
     * Profiler currently displayed in the debug screen pie chart
     */
    private String debugProfilerName = "root";

    public static final boolean DEBUG_MODE = false;
    public GuiMainMenu mainMenu;

    public Minecraft(GameConfiguration gameConfig) {
        instance = this;
        this.gameDir = gameConfig.folderInfo.gameDir;
        this.fileAssets = gameConfig.folderInfo.assetsDir;
        this.fileResourcepacks = gameConfig.folderInfo.resourcePacksDir;
        this.launchedVersion = gameConfig.gameInfo.version;
        this.versionType = gameConfig.gameInfo.versionType;
        this.profileProperties = gameConfig.userInfo.profileProperties;
        this.defaultResourcePack = new DefaultResourcePack(gameConfig.folderInfo.getAssetsIndex());
        this.proxy = gameConfig.userInfo.proxy == null ? Proxy.NO_PROXY : gameConfig.userInfo.proxy;
        this.sessionService = (new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString())).createMinecraftSessionService();
        this.session = gameConfig.userInfo.session;
        LOGGER.info("Setting user: {}", this.session.getUsername());
        LOGGER.debug("(Session ID is {})", this.session.getSessionID());
        this.displayWidth = gameConfig.displayInfo.width > 0 ? gameConfig.displayInfo.width : 1;
        this.displayHeight = gameConfig.displayInfo.height > 0 ? gameConfig.displayInfo.height : 1;
        this.tempDisplayWidth = gameConfig.displayInfo.width;
        this.tempDisplayHeight = gameConfig.displayInfo.height;
        this.fullscreen = gameConfig.displayInfo.fullscreen;
        this.jvm64bit = isJvm64bit();
        this.integratedServer = null;

        if (gameConfig.serverInfo.serverName != null) {
            this.serverName = gameConfig.serverInfo.serverName;
            this.serverPort = gameConfig.serverInfo.serverPort;
        }

        ImageIO.setUseCache(false);
        Locale.setDefault(Locale.ROOT);
        Bootstrap.register();
        TextComponentKeybind.displaySupplierFunction = KeyBinding::getDisplayString;
        this.dataFixer = DataFixesManager.createFixer();
        this.toastGui = new GuiToast(this);
    }

    public void run() {
        this.running = true;

        try {
            this.init();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Initializing game");
            crashreport.makeCategory("Initialization");
            this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(crashreport));
            return;
        }

        try {
            while (this.running) {
                if (!this.hasCrashed || this.crashReporter == null) {
                    try {
                        this.runGameLoop();
                    } catch (OutOfMemoryError e) {
                        this.freeMemory();
                        this.displayGuiScreen(new GuiMemoryErrorScreen());
                        System.gc();
                    }
                } else {
                    this.displayCrashReport(this.crashReporter);
                }
            }
        } catch (MinecraftError ignored) {
        } catch (ReportedException reportedException) {
            this.addGraphicsAndWorldToCrashReport(reportedException.getCrashReport());
            this.freeMemory();
            LOGGER.fatal("Reported exception thrown!", reportedException);
            this.displayCrashReport(reportedException.getCrashReport());
        } catch (Throwable throwable) {
            CrashReport crashReport = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable));
            this.freeMemory();
            LOGGER.fatal("Unreported exception thrown!", throwable);
            this.displayCrashReport(crashReport);
        } finally {
            this.shutdownMinecraftApplet();
        }
    }

    /**
     * Starts the game: initializes the canvas, the title, the settings, etcetera.
     */
    private void init() {
        this.gameSettings = new GameSettings(this, this.gameDir);
        this.creativeSettings = new CreativeSettings(this, this.gameDir);
        this.defaultResourcePacks.add(this.defaultResourcePack);

        if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
            this.displayWidth = this.gameSettings.overrideWidth;
            this.displayHeight = this.gameSettings.overrideHeight;
        }

        LOGGER.info("LWJGL Version: {}", Sys.getVersion());
        this.setWindowIcon();
        this.setInitialDisplayMode();
        this.createDisplay();
        OpenGlHelper.initializeTextures();
        this.framebuffer = new Framebuffer(this.displayWidth, this.displayHeight, true);
        this.framebuffer.setFramebufferColor(0.0F, 0.0F, 0.0F, 0.0F);
        this.registerMetadataSerializers();
        this.resourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.gameDir, "server-resource-packs"), this.defaultResourcePack, this.metadataSerializer, this.gameSettings);
        this.resourceManager = new SimpleReloadableResourceManager(this.metadataSerializer);
        this.languageManager = new LanguageManager(this.metadataSerializer, this.gameSettings.language);
        this.resourceManager.registerReloadListener(this.languageManager);
        this.refreshResources();
        this.renderEngine = new TextureManager(this.resourceManager);
        this.resourceManager.registerReloadListener(this.renderEngine);
        this.drawSplashScreen(this.renderEngine);
        this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
        this.saveLoader = new AnvilSaveConverter(new File(this.gameDir, "saves"), this.dataFixer);
        this.soundHandler = new SoundHandler(this.resourceManager, this.gameSettings);
        this.resourceManager.registerReloadListener(this.soundHandler);
        this.musicTicker = new MusicTicker(this);
        this.fontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);

        if (this.gameSettings.language != null) {
            this.fontRenderer.setUnicodeFlag(this.isUnicode());
            this.fontRenderer.setBidiFlag(this.languageManager.isCurrentLanguageBidirectional());
        }

        this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
        this.resourceManager.registerReloadListener(this.fontRenderer);
        this.resourceManager.registerReloadListener(this.standardGalacticFontRenderer);
        this.resourceManager.registerReloadListener(new GrassColorReloadListener());
        this.resourceManager.registerReloadListener(new FoliageColorReloadListener());
        this.mouseHelper = new MouseHelper();
        this.checkGLError("Pre startup");
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7425);
        GlStateManager.clearDepth(1.0D);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        this.checkGLError("Startup");
        this.textureMapBlocks = new TextureMap("textures");
        this.textureMapBlocks.setMipmapLevels(this.gameSettings.mipmapLevels);
        this.renderEngine.loadTickableTexture(TextureMap.LOCATION_BLOCKS_TEXTURE, this.textureMapBlocks);
        this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
        ModelManager modelManager = new ModelManager(this.textureMapBlocks);
        this.resourceManager.registerReloadListener(modelManager);
        this.blockColors = BlockColors.init();
        ItemColors itemColors = ItemColors.init(this.blockColors);
        this.renderItem = new RenderItem(this.renderEngine, modelManager, itemColors);
        this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
        this.itemRenderer = new ItemRenderer(this);
        this.resourceManager.registerReloadListener(this.renderItem);
        this.entityRenderer = new EntityRenderer(this, this.resourceManager);
        this.resourceManager.registerReloadListener(this.entityRenderer);
        this.blockRenderDispatcher = new BlockRendererDispatcher(modelManager.getBlockModelShapes(), this.blockColors);
        this.resourceManager.registerReloadListener(this.blockRenderDispatcher);
        this.renderGlobal = new RenderGlobal(this);
        this.resourceManager.registerReloadListener(this.renderGlobal);
        this.populateSearchTreeManager();
        this.resourceManager.registerReloadListener(this.searchTreeManager);
        GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
        this.effectRenderer = new ParticleManager(this.world, this.renderEngine);
        this.checkGLError("Post startup");
        this.ingameGUI = new GuiIngame(this);

        if (DEBUG_MODE) {
            this.mainMenu = new DebugMainMenu();
        } else {
            this.mainMenu = new MINMainMenu();
        }

        if (this.serverName != null) {
            this.displayGuiScreen(new GuiDamnJapaneseAction(new GuiConnecting(this.mainMenu, this, this.serverName, this.serverPort)));
        } else {
            this.displayGuiScreen(new GuiLoading());
        }
        this.renderEngine.deleteTexture(this.mojangLogo);
        this.mojangLogo = null;
        this.loadingScreen = new LoadingScreenRenderer(this);
        this.debugRenderer = new DebugRenderer(this);

        if (this.gameSettings.fullScreen && !this.fullscreen) {
            this.toggleFullscreen();
        }

        Display.setVSyncEnabled(this.gameSettings.enableVsync);

        this.renderGlobal.makeEntityOutlineShader();
        MIN.init();
    }

    /**
     * Fills {@link #searchTreeManager} with the current item and recipe registry contents.
     */
    private void populateSearchTreeManager() {
        SearchTree<ItemStack> searchTree = new SearchTree<>((itemStack) ->
                itemStack.getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL)
                        .stream()
                        .map(TextFormatting::getTextWithoutFormattingCodes)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(toolTip -> !toolTip.isEmpty()).collect(Collectors.toList()), itemStack ->
                Collections.singleton(Item.REGISTRY.getNameForObject(itemStack.getItem())));
        NonNullList<ItemStack> nonnulllist = NonNullList.create();

        for (Item item : Item.REGISTRY) {
            item.getSubItems(CreativeTabs.SEARCH, nonnulllist);
        }

        nonnulllist.forEach(searchTree::add);
        SearchTree<RecipeList> searchTree1 = new SearchTree<>((recipeList) ->
                recipeList.getRecipes().stream()
                        .flatMap(recipe -> recipe.getRecipeOutput().getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL).stream())
                        .map(TextFormatting::getTextWithoutFormattingCodes)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(recipe -> !recipe.isEmpty()).collect(Collectors.toList()), (recipeList) ->
                recipeList.getRecipes().stream().map(recipe -> Item.REGISTRY.getNameForObject(recipe.getRecipeOutput().getItem())).collect(Collectors.toList()));
        RecipeBookClient.ALL_RECIPES.forEach(searchTree1::add);
        this.searchTreeManager.register(SearchTreeManager.ITEMS, searchTree);
        this.searchTreeManager.register(SearchTreeManager.RECIPES, searchTree1);
    }

    private void registerMetadataSerializers() {
        this.metadataSerializer.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        this.metadataSerializer.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        this.metadataSerializer.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        this.metadataSerializer.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        this.metadataSerializer.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    }

    private void createDisplay() {
        if (DEBUG_MODE) {
            Display.setTitle("MIN Client(Minecraft 1.12.2) - DEBUG MODE");
        } else {
            Display.setTitle("MIN Client(Minecraft 1.12.2) - Release " + MIN.VERSION);
        }

        try {
            Display.create();
        } catch (Exception e) {
            LOGGER.error("Couldn't set pixel format", e);

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) {
            }

            if (this.fullscreen) {
                this.updateDisplayMode();
            }

            Display.create();
        }
    }

    private void setInitialDisplayMode() {
        if (this.fullscreen) {
            Display.setFullscreen(true);
            DisplayMode displaymode = Display.getDisplayMode();
            this.displayWidth = Math.max(1, displaymode.getWidth());
            this.displayHeight = Math.max(1, displaymode.getHeight());
        } else {
            Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
        }
    }

    private void setWindowIcon() {
        Util.EnumOS osType = Util.getOSType();

        if (osType != Util.EnumOS.OSX) {
            InputStream icon16 = null;
            InputStream icon32 = null;

            try {
                icon16 = this.defaultResourcePack.getResourceStream(new ResourceLocation("min/icon_16x16.png"));
                icon32 = this.defaultResourcePack.getResourceStream(new ResourceLocation("min/icon_32x32.png"));

                if (icon16 != null && icon32 != null) {
                    Display.setIcon(new ByteBuffer[]{this.readImageToBuffer(icon16), this.readImageToBuffer(icon32)});
                }
            } catch (IOException e) {
                LOGGER.error("Couldn't set icon", e);
            } finally {
                IOUtils.closeQuietly(icon16);
                IOUtils.closeQuietly(icon32);
            }
        }
    }

    private static boolean isJvm64bit() {
        String[] archArray = new String[]{"sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"};

        for (String arch : archArray) {
            String property = System.getProperty(arch);

            if (property != null && property.contains("64")) {
                return true;
            }
        }

        return false;
    }

    public Framebuffer getFramebuffer() {
        return this.framebuffer;
    }

    /**
     * Gets the version that Minecraft was launched under (the name of a version JSON). Specified via the
     * <code>--version</code> flag.
     */
    public String getVersion() {
        return this.launchedVersion;
    }

    /**
     * Gets the type of version that Minecraft was launched under (as specified in the version JSON). Specified via the
     * <code>--versionType</code> flag.
     */
    public String getVersionType() {
        return this.versionType;
    }

    public void crashed(CrashReport crash) {
        this.hasCrashed = true;
        this.crashReporter = crash;
    }

    /**
     * Wrapper around displayCrashReportInternal
     */
    public void displayCrashReport(CrashReport crashReportIn) {
        File file1 = new File(getMinecraft().gameDir, "crash-reports");
        File file2 = new File(file1, "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-client.txt");
        Bootstrap.printToSYSOUT(crashReportIn.getCompleteReport());

        if (crashReportIn.getFile() != null) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
            System.exit(-1);
        } else if (crashReportIn.saveToFile(file2)) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
            System.exit(-1);
        } else {
            Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }

    public boolean isUnicode() {
        return this.languageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
    }

    public void refreshResources() {
        List<IResourcePack> list = Lists.newArrayList(this.defaultResourcePacks);

        if (this.integratedServer != null) {
            this.integratedServer.reload();
        }

        for (ResourcePackRepository.Entry resourcepackrepository$entry : this.resourcePackRepository.getRepositoryEntries()) {
            list.add(resourcepackrepository$entry.getResourcePack());
        }

        if (this.resourcePackRepository.getServerResourcePack() != null) {
            list.add(this.resourcePackRepository.getServerResourcePack());
        }

        try {
            this.resourceManager.reloadResources(list);
        } catch (RuntimeException runtimeexception) {
            LOGGER.info("Caught error stitching, removing all assigned resourcepacks", runtimeexception);
            list.clear();
            list.addAll(this.defaultResourcePacks);
            this.resourcePackRepository.setRepositories(Collections.emptyList());
            this.resourceManager.reloadResources(list);
            this.gameSettings.resourcePacks.clear();
            this.gameSettings.incompatibleResourcePacks.clear();
            this.gameSettings.saveOptions();
        }

        this.languageManager.parseLanguageMetadata(list);

        if (this.renderGlobal != null) {
            this.renderGlobal.loadRenderers();
        }
    }

    private ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(imageStream);
        int[] rgb = bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null, 0, bufferedImage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * rgb.length);

        for (int i : rgb) {
            bytebuffer.putInt(i << 8 | i >> 24 & 255);
        }

        bytebuffer.flip();
        return bytebuffer;
    }

    private void updateDisplayMode() {
        Set<DisplayMode> set = Sets.newHashSet();
        Collections.addAll(set, Display.getAvailableDisplayModes());
        DisplayMode current = Display.getDesktopDisplayMode();

        if (!set.contains(current) && Util.getOSType() == Util.EnumOS.OSX) {
            label52:

            for (DisplayMode displayMode : MAC_DISPLAY_MODES) {
                boolean flag = true;

                for (DisplayMode matchDisplayMode : set) {
                    if (matchDisplayMode.getBitsPerPixel() == 32 && matchDisplayMode.getWidth() == displayMode.getWidth() && matchDisplayMode.getHeight() == displayMode.getHeight()) {
                        flag = false;
                        break;
                    }
                }

                if (!flag) {
                    Iterator<DisplayMode> iterator = set.iterator();
                    DisplayMode toSet;

                    do {
                        if (!iterator.hasNext()) {
                            continue label52;
                        }

                        toSet = iterator.next();

                    } while (toSet.getBitsPerPixel() != 32 || toSet.getWidth() != displayMode.getWidth() / 2 || toSet.getHeight() != displayMode.getHeight() / 2);

                    current = toSet;
                }
            }
        }

        Display.setDisplayMode(current);
        this.displayWidth = current.getWidth();
        this.displayHeight = current.getHeight();
    }

    private void drawSplashScreen(TextureManager textureManagerInstance) {
        ScaledResolution scaledresolution = new ScaledResolution(this);
        int scaleFactor = scaledresolution.getScaleFactor();
        Framebuffer framebuffer = new Framebuffer(scaledresolution.getScaledWidth() * scaleFactor, scaledresolution.getScaledHeight() * scaleFactor, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        InputStream inputstream = null;

        try {
            inputstream = this.defaultResourcePack.getInputStream(LOCATION_MOJANG_PNG);
            this.mojangLogo = textureManagerInstance.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(inputstream)));
            textureManagerInstance.bindTexture(this.mojangLogo);
        } catch (IOException e) {
            LOGGER.error("Unable to load logo: {}", LOCATION_MOJANG_PNG, e);
        } finally {
            IOUtils.closeQuietly(inputstream);
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, this.displayHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(this.displayWidth, this.displayHeight, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(this.displayWidth, 0.0D, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.draw((scaledresolution.getScaledWidth() - 256) / 2, (scaledresolution.getScaledHeight() - 256) / 2, 0, 0, 256, 256, 255, 255, 255, 255);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(scaledresolution.getScaledWidth() * scaleFactor, scaledresolution.getScaledHeight() * scaleFactor);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        Gui.drawRect(0, 0, displayWidth, displayHeight, 0);
        this.updateDisplay();
    }

    /**
     * Draw with the WorldRenderer
     */
    public void draw(int posX, int posY, int texU, int texV, int width, int height, int red, int green, int blue, int alpha) {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(posX, posY + height, 0.0D).tex((float) texU * 0.00390625F, (float) (texV + height) * 0.00390625F).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX + width, posY + height, 0.0D).tex((float) (texU + width) * 0.00390625F, (float) (texV + height) * 0.00390625F).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX + width, posY, 0.0D).tex((float) (texU + width) * 0.00390625F, (float) texV * 0.00390625F).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX, posY, 0.0D).tex((float) texU * 0.00390625F, (float) texV * 0.00390625F).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

    /**
     * Returns the save loader that is currently being used
     */
    public ISaveFormat getSaveLoader() {
        return this.saveLoader;
    }

    /**
     * Sets the argument GuiScreen as the main (topmost visible) screen.
     *
     * <p><strong>WARNING</strong>: This method is not thread-safe. Opening GUIs from a thread other than the main
     * thread may cause many different issues, including the GUI being rendered before it has initialized (leading to
     * unusual crashes). If on a thread other than the main thread, use {@link #addScheduledTask}:
     *
     * <pre>
     * minecraft.addScheduledTask(() -> minecraft.displayGuiScreen(gui));
     * </pre>
     *
     * @param guiScreenIn The {@link GuiScreen} to display. If it is {@code null}, any open GUI will be closed.
     */
    @SuppressWarnings("all")
    public void displayGuiScreen(@Nullable GuiScreen guiScreenIn) {
        if (this.currentScreen != null) {
            this.currentScreen.onGuiClosed();
        }

        if (guiScreenIn == null && this.world == null) {
            guiScreenIn = this.mainMenu;
        } else if (guiScreenIn == null && this.player.getHealth() <= 0.0F) {
            guiScreenIn = new GuiGameOver(null);
        }

        if (guiScreenIn instanceof GuiMainMenu || guiScreenIn instanceof GuiMultiplayer) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages(true);
        }

        this.currentScreen = guiScreenIn;

        if (guiScreenIn != null) {
            this.setIngameNotInFocus();
            KeyBinding.unPressAllKeys();

            while (Mouse.next()) {
            }

            while (Keyboard.next()) {
            }

            ScaledResolution scaledresolution = new ScaledResolution(this);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(this, i, j);
            this.skipRenderWorld = false;
        } else {
            this.soundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }

    /**
     * Checks for an OpenGL error. If there is one, prints the error ID and error string.
     */
    private void checkGLError(String message) {
        int error = GlStateManager.glGetError();

        if (error != 0) {
            String s = GLU.gluErrorString(error);
            LOGGER.error("########## GL ERROR ##########");
            LOGGER.error("@ {}", message);
            LOGGER.error("{}: {}", error, s);
        }
    }

    /**
     * Shuts down the minecraft applet by stopping the resource downloads, and clearing up GL stuff; called when the
     * application (or web page) is exited.
     */
    public void shutdownMinecraftApplet() {
        try {
            LOGGER.info("Stopping!");
            MIN.stop();

            try {
                this.loadWorld(null);
            } catch (Throwable ignored) {
            }

            this.soundHandler.unloadSounds();
        } finally {
            Display.destroy();

            if (!this.hasCrashed) {
                System.exit(0);
            }
        }

        System.gc();
    }

    /**
     * Called repeatedly from run()
     */
    private void runGameLoop() throws IOException {
        long i = System.nanoTime();
        this.profiler.startSection("root");

        if (Display.isCreated() && Display.isCloseRequested()) {
            this.shutdown();
        }

        this.timer.updateTimer();
        this.profiler.startSection("scheduledExecutables");

        synchronized (this.scheduledTasks) {
            while (!this.scheduledTasks.isEmpty()) {
                Util.runTask(this.scheduledTasks.poll(), LOGGER);
            }
        }

        this.profiler.endSection();
        this.profiler.startSection("tick");

        for (int j = 0; j < this.timer.elapsedTicks; ++j) {
            this.runTick();
        }

        this.profiler.endStartSection("preRenderErrors");
        this.checkGLError("Pre render");
        this.profiler.endStartSection("sound");
        this.soundHandler.setListener(this.player, this.timer.renderPartialTicks);
        this.profiler.endSection();
        this.profiler.startSection("render");
        GlStateManager.pushMatrix();
        GlStateManager.clear(16640);
        this.framebuffer.bindFramebuffer(true);
        this.profiler.startSection("display");
        GlStateManager.enableTexture2D();
        this.profiler.endSection();

        if (!this.skipRenderWorld) {
            this.profiler.endStartSection("gameRenderer");
            this.entityRenderer.updateCameraAndRender(this.isGamePaused ? this.renderPartialTicksPaused : this.timer.renderPartialTicks, i);
            this.profiler.endStartSection("toasts");
            this.toastGui.drawToast(new ScaledResolution(this));
            this.profiler.endSection();
        }

        this.profiler.endSection();

        if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.hideGUI) {
            if (!this.profiler.profilingEnabled) {
                this.profiler.clearProfiling();
            }

            this.profiler.profilingEnabled = true;
            this.displayDebugInfo();
        } else {
            this.profiler.profilingEnabled = false;
            this.prevFrameTime = System.nanoTime();
        }

        this.framebuffer.unbindFramebuffer();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.framebuffer.framebufferRender(this.displayWidth, this.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.entityRenderer.renderStreamIndicator();
        GlStateManager.popMatrix();
        this.profiler.startSection("root");
        this.updateDisplay();
        Thread.yield();
        this.checkGLError("Post render");
        ++this.fpsCounter;
        boolean canPauseGame = this.isSingleplayer() && this.currentScreen != null && this.currentScreen.doesGuiPauseGame() && !Objects.requireNonNull(this.integratedServer).getPublic();

        if (this.isGamePaused != canPauseGame) {
            if (this.isGamePaused) {
                this.renderPartialTicksPaused = this.timer.renderPartialTicks;
            } else {
                this.timer.renderPartialTicks = this.renderPartialTicksPaused;
            }

            this.isGamePaused = canPauseGame;
        }

        long k = System.nanoTime();
        this.frameTimer.addFrame(k - this.startNanoTime);
        this.startNanoTime = k;

        while (getSystemTime() >= this.debugUpdateTime + 1000L) {
            debugFPS = this.fpsCounter;
            this.debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", debugFPS, RenderChunk.renderChunksUpdated, RenderChunk.renderChunksUpdated == 1 ? "" : "s", (float) this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax() ? "inf" : this.gameSettings.limitFramerate, this.gameSettings.enableVsync ? " vsync" : "", this.gameSettings.fancyGraphics ? "" : " fast", this.gameSettings.clouds == 0 ? "" : (this.gameSettings.clouds == 1 ? " fast-clouds" : " fancy-clouds"), OpenGlHelper.useVbo() ? " vbo" : "");
            RenderChunk.renderChunksUpdated = 0;
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
            this.usageSnooper.addMemoryStatsToSnooper();

            if (!this.usageSnooper.isSnooperRunning()) {
                this.usageSnooper.startSnooper();
            }
        }

        if (this.isFramerateLimitBelowMax()) {
            this.profiler.startSection("fpslimit_wait");
            Display.sync(this.getLimitFramerate());
            this.profiler.endSection();
        }

        this.profiler.endSection();
    }

    public void updateDisplay() {
        this.profiler.startSection("display_update");
        Display.update();
        this.profiler.endSection();
        this.checkWindowResize();
    }

    protected void checkWindowResize() {
        if (!this.fullscreen && Display.wasResized()) {
            int i = this.displayWidth;
            int j = this.displayHeight;
            this.displayWidth = Display.getWidth();
            this.displayHeight = Display.getHeight();

            if (this.displayWidth != i || this.displayHeight != j) {
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }

                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }

                this.resize(this.displayWidth, this.displayHeight);
            }
        }
    }

    public int getLimitFramerate() {
        return this.world == null && this.currentScreen != null ? 30 : this.gameSettings.limitFramerate;
    }

    public boolean isFramerateLimitBelowMax() {
        return (float) this.getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
    }

    /**
     * Attempts to free as much memory as possible, including leaving the world and running the garbage collector.
     */
    public void freeMemory() {
        try {
            memoryReserve = new byte[0];
            this.renderGlobal.deleteAllDisplayLists();
        } catch (Throwable ignored) {
        }

        try {
            System.gc();
            this.loadWorld(null);
        } catch (Throwable ignored) {
        }

        System.gc();
    }

    /**
     * Update debugProfilerName in response to number keys in debug screen
     */
    private void updateDebugProfilerName(int keyCount) {
        List<Profiler.Result> list = this.profiler.getProfilingData(this.debugProfilerName);

        if (!list.isEmpty()) {
            Profiler.Result profiler$result = list.remove(0);

            if (keyCount == 0) {
                if (!profiler$result.profilerName.isEmpty()) {
                    int i = this.debugProfilerName.lastIndexOf(46);

                    if (i >= 0) {
                        this.debugProfilerName = this.debugProfilerName.substring(0, i);
                    }
                }
            } else {
                --keyCount;

                if (keyCount < list.size() && !"unspecified".equals((list.get(keyCount)).profilerName)) {
                    if (!this.debugProfilerName.isEmpty()) {
                        this.debugProfilerName = this.debugProfilerName + ".";
                    }

                    this.debugProfilerName = this.debugProfilerName + (list.get(keyCount)).profilerName;
                }
            }
        }
    }

    /**
     * Parameter appears to be unused
     */
    private void displayDebugInfo() {
        if (this.profiler.profilingEnabled) {
            List<Profiler.Result> list = this.profiler.getProfilingData(this.debugProfilerName);
            Profiler.Result profiler$result = list.remove(0);
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0D, this.displayWidth, this.displayHeight, 0.0D, 1000.0D, 3000.0D);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -2000.0F);
            GlStateManager.glLineWidth(1.0F);
            GlStateManager.disableTexture2D();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            int j = this.displayWidth - 160 - 10;
            int k = this.displayHeight - 320;
            GlStateManager.enableBlend();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos((float) j - 176.0F, (float) k - 96.0F - 16.0F, 0.0D).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos((float) j - 176.0F, k + 320, 0.0D).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos((float) j + 176.0F, k + 320, 0.0D).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos((float) j + 176.0F, (float) k - 96.0F - 16.0F, 0.0D).color(200, 0, 0, 0).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            double d0 = 0.0D;

            for (Profiler.Result result : list) {
                int i1 = MathHelper.floor(result.usePercentage / 4.0D) + 1;
                bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
                int j1 = result.getColor();
                int k1 = j1 >> 16 & 255;
                int l1 = j1 >> 8 & 255;
                int i2 = j1 & 255;
                bufferbuilder.pos(j, k, 0.0D).color(k1, l1, i2, 255).endVertex();

                for (int j2 = i1; j2 >= 0; --j2) {
                    float f = (float) ((d0 + result.usePercentage * (double) j2 / (double) i1) * (Math.PI * 2D) / 100.0D);
                    float f1 = MathHelper.sin(f) * 160.0F;
                    float f2 = MathHelper.cos(f) * 160.0F * 0.5F;
                    bufferbuilder.pos((float) j + f1, (float) k - f2, 0.0D).color(k1, l1, i2, 255).endVertex();
                }

                tessellator.draw();
                bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);

                for (int i3 = i1; i3 >= 0; --i3) {
                    float f3 = (float) ((d0 + result.usePercentage * (double) i3 / (double) i1) * (Math.PI * 2D) / 100.0D);
                    float f4 = MathHelper.sin(f3) * 160.0F;
                    float f5 = MathHelper.cos(f3) * 160.0F * 0.5F;
                    bufferbuilder.pos((float) j + f4, (float) k - f5, 0.0D).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
                    bufferbuilder.pos((float) j + f4, (float) k - f5 + 10.0F, 0.0D).color(k1 >> 1, l1 >> 1, i2 >> 1, 255).endVertex();
                }

                tessellator.draw();
                d0 += result.usePercentage;
            }

            DecimalFormat decimalformat = new DecimalFormat("##0.00");
            GlStateManager.enableTexture2D();
            String s = "";

            if (!"unspecified".equals(profiler$result.profilerName)) {
                s = s + "[0] ";
            }

            if (profiler$result.profilerName.isEmpty()) {
                s = s + "ROOT ";
            } else {
                s = s + profiler$result.profilerName + ' ';
            }

            this.fontRenderer.drawStringWithShadow(s, (float) (j - 160), (float) (k - 80 - 16), 16777215);
            s = decimalformat.format(profiler$result.totalUsePercentage) + "%";
            this.fontRenderer.drawStringWithShadow(s, (float) (j + 160 - this.fontRenderer.getStringWidth(s)), (float) (k - 80 - 16), 16777215);

            for (int k2 = 0; k2 < list.size(); ++k2) {
                Profiler.Result profiler$result2 = list.get(k2);
                StringBuilder stringbuilder = new StringBuilder();

                if ("unspecified".equals(profiler$result2.profilerName)) {
                    stringbuilder.append("[?] ");
                } else {
                    stringbuilder.append("[").append(k2 + 1).append("] ");
                }

                String s1 = stringbuilder.append(profiler$result2.profilerName).toString();
                this.fontRenderer.drawStringWithShadow(s1, (float) (j - 160), (float) (k + 80 + k2 * 8 + 20), profiler$result2.getColor());
                s1 = decimalformat.format(profiler$result2.usePercentage) + "%";
                this.fontRenderer.drawStringWithShadow(s1, (float) (j + 160 - 50 - this.fontRenderer.getStringWidth(s1)), (float) (k + 80 + k2 * 8 + 20), profiler$result2.getColor());
                s1 = decimalformat.format(profiler$result2.totalUsePercentage) + "%";
                this.fontRenderer.drawStringWithShadow(s1, (float) (j + 160 - this.fontRenderer.getStringWidth(s1)), (float) (k + 80 + k2 * 8 + 20), profiler$result2.getColor());
            }
        }
    }

    /**
     * Called when the window is closing. Sets 'running' to false which allows the game loop to exit cleanly.
     */
    public void shutdown() {
        this.running = false;
    }

    /**
     * Will set the focus to ingame if the Minecraft window is the active with focus. Also clears any GUI screen
     * currently displayed
     */
    public void setIngameFocus() {
        if (Display.isActive()) {
            if (!this.inGameHasFocus) {
                if (!IS_RUNNING_ON_MAC) {
                    KeyBinding.updateKeyBindState();
                }

                this.inGameHasFocus = true;
                this.mouseHelper.grabMouseCursor();
                this.displayGuiScreen(null);
                this.leftClickCounter = 10000;
            }
        }
    }

    /**
     * Resets the player keystate, disables the ingame focus, and ungrabs the mouse cursor.
     */
    public void setIngameNotInFocus() {
        if (this.inGameHasFocus) {
            this.inGameHasFocus = false;
            this.mouseHelper.ungrabMouseCursor();
        }
    }

    /**
     * Displays the ingame menu
     */
    public void displayInGameMenu() {
        if (this.currentScreen == null) {
            this.displayGuiScreen(new GuiIngameMenu());

            if (this.isSingleplayer() && !Objects.requireNonNull(this.integratedServer).getPublic()) {
                this.soundHandler.pauseSounds();
            }
        }
    }

    private void sendClickBlockToController(boolean leftClick) {
        if (!leftClick) {
            this.leftClickCounter = 0;
        }

        if (this.leftClickCounter <= 0 && !this.player.isHandActive()) {
            if (leftClick && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = this.objectMouseOver.getBlockPos();

                assert blockpos != null;
                if (this.world.getBlockState(blockpos).getMaterial() != Material.AIR) {
                    assert this.objectMouseOver.sideHit != null;
                    if (this.playerController.onPlayerDamageBlock(blockpos, this.objectMouseOver.sideHit)) {
                        this.effectRenderer.addBlockHitEffects(blockpos, this.objectMouseOver.sideHit);
                        this.player.swingArm(EnumHand.MAIN_HAND, true);
                    }
                }
            } else {
                this.playerController.resetBlockRemoving();
            }
        }
    }

    private void clickMouse() {
        if (this.leftClickCounter <= 0) {
            KeyStrokes.leftCounter.add(System.currentTimeMillis());
            if (this.objectMouseOver == null) {
                LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");

                if (this.playerController.isNotCreative()) {
                    this.leftClickCounter = 10;
                }
            } else if (!this.player.isRowingBoat()) {
                switch (this.objectMouseOver.typeOfHit) {
                    case ENTITY:
                        assert this.objectMouseOver.entityHit != null;
                        this.playerController.attackEntity(this.player, this.objectMouseOver.entityHit);
                        break;

                    case BLOCK:
                        BlockPos blockpos = this.objectMouseOver.getBlockPos();

                        assert blockpos != null;
                        if (this.world.getBlockState(blockpos).getMaterial() != Material.AIR) {
                            assert this.objectMouseOver.sideHit != null;
                            this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
                            break;
                        }

                    case MISS:
                        if (this.playerController.isNotCreative()) {
                            this.leftClickCounter = 10;
                        }

                        this.player.resetCooldown();
                }

                this.player.swingArm(EnumHand.MAIN_HAND, true);
            }
        }
    }


    /**
     * Called when user clicked he's mouse right button (place)
     */
    @SuppressWarnings("incomplete-switch")
    private void rightClickMouse() {
        if (!this.playerController.getIsHittingBlock() || Managers.moduleManager.miscModules.get("TerminateBreakingBlock").isEnabled()) {
            this.rightClickDelayTimer = 4;

            if (!this.player.isRowingBoat()) {
                if (this.objectMouseOver == null) {
                    LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
                }

                for (EnumHand enumhand : EnumHand.values()) {
                    ItemStack itemstack = this.player.getHeldItem(enumhand);

                    if (this.objectMouseOver != null) {
                        switch (this.objectMouseOver.typeOfHit) {
                            case ENTITY -> {
                                assert this.objectMouseOver.entityHit != null;
                                if (this.playerController.interactWithEntity(this.player, this.objectMouseOver.entityHit, this.objectMouseOver, enumhand) == EnumActionResult.SUCCESS) {
                                    return;
                                }
                                if (this.playerController.interactWithEntity(this.player, this.objectMouseOver.entityHit, enumhand) == EnumActionResult.SUCCESS) {
                                    return;
                                }
                            }
                            case BLOCK -> {
                                BlockPos blockpos = this.objectMouseOver.getBlockPos();
                                assert blockpos != null;
                                if (this.world.getBlockState(blockpos).getMaterial() != Material.AIR) {
                                    int i = itemstack.getCount();
                                    assert this.objectMouseOver.sideHit != null;
                                    EnumActionResult enumactionresult = this.playerController.processRightClickBlock(this.player, this.world, blockpos, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec, enumhand);

                                    if (enumactionresult == EnumActionResult.SUCCESS) {
                                        this.player.swingArm(enumhand, true);

                                        if (!itemstack.isEmpty() && (itemstack.getCount() != i || this.playerController.isInCreativeMode())) {
                                            this.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                        }

                                        return;
                                    }
                                }
                            }
                        }
                    }

                    if (!itemstack.isEmpty() && this.playerController.processRightClick(this.player, this.world, enumhand) == EnumActionResult.SUCCESS) {
                        this.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Toggles fullscreen mode.
     */
    public void toggleFullscreen() {
        try {
            this.fullscreen = !this.fullscreen;
            this.gameSettings.fullScreen = this.fullscreen;

            if (this.fullscreen) {
                this.updateDisplayMode();
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();

            } else {
                Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
                this.displayWidth = this.tempDisplayWidth;
                this.displayHeight = this.tempDisplayHeight;

            }
            if (this.displayWidth <= 0) {
                this.displayWidth = 1;
            }
            if (this.displayHeight <= 0) {
                this.displayHeight = 1;
            }

            if (this.currentScreen != null) {
                this.resize(this.displayWidth, this.displayHeight);
            } else {
                this.updateFramebufferSize();
            }

            Display.setFullscreen(this.fullscreen);
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
            this.updateDisplay();
        } catch (Exception exception) {
            LOGGER.error("Couldn't toggle fullscreen", exception);
        }
    }

    /**
     * Called to resize the current screen.
     */
    private void resize(int width, int height) {
        this.displayWidth = Math.max(1, width);
        this.displayHeight = Math.max(1, height);

        if (this.currentScreen != null) {
            ScaledResolution scaledresolution = new ScaledResolution(this);
            this.currentScreen.onResize(this, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight());
        }

        this.loadingScreen = new LoadingScreenRenderer(this);
        this.updateFramebufferSize();
    }

    private void updateFramebufferSize() {
        this.framebuffer.createBindFramebuffer(this.displayWidth, this.displayHeight);

        if (this.entityRenderer != null) {
            this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
        }
    }

    /**
     * Return the musicTicker's instance
     */
    public MusicTicker getMusicTicker() {
        return this.musicTicker;
    }

    /**
     * Runs the current tick.
     */
    public void runTick() throws IOException {
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }

        HYTChunkExecutor.tick();

        if (this.world != null) {
            Managers.moduleManager.boostModules.values().stream().filter(Module::isEnabled).forEach(BoostModule::tick);
            Managers.moduleManager.miscModules.values().stream().filter(Module::isEnabled).forEach(MiscModule::tick);
            Spinning.current += Spinning.speed.getValue() * (Spinning.direction.isCurrentMode("P") ? 1 : -1);
            if (this.player.ticksExisted % 1200 == 0) {
                MIN.runAsync(Managers.fileManager::saveConfig);
                MIN.runAsync(() -> {
                    if (this.player != null) {
                        try {
                            WebUtil.getJSONFromPost("https://minserver.vlouboos.repl.co/online/activate?username=" + this.player.getName() + "&uuid=" + this.player.getUniqueID());
                        } catch (URISyntaxException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
            if (this.player.ticksExisted == 5) {
                IRCMessageGrabber.reset();
            }
            if (this.player.ticksExisted % 60 == 0) {
                MIN.checkIfAsyncThreadAlive();
                IRCMessageGrabber.grabMessage();
            }
        }

        this.profiler.startSection("gui");

        if (!this.isGamePaused) {
            this.ingameGUI.updateTick();
        }

        this.profiler.endSection();
        this.entityRenderer.getMouseOver(1.0F);
        this.profiler.startSection("gameMode");

        if (!this.isGamePaused && this.world != null) {
            this.playerController.updateController();
        }

        this.profiler.endStartSection("textures");

        if (this.world != null) {
            this.renderEngine.tick();
        }

        if (this.currentScreen == null && this.player != null) {
            if (this.player.getHealth() <= 0.0F && !(this.currentScreen instanceof GuiGameOver)) {
                this.displayGuiScreen(null);
            } else if (this.player.isPlayerSleeping() && this.world != null) {
                this.displayGuiScreen(new GuiSleepMP());
            }
        } else if (this.currentScreen != null && this.currentScreen instanceof GuiSleepMP && !this.player.isPlayerSleeping()) {
            this.displayGuiScreen(null);
        }

        if (this.currentScreen != null) {
            this.leftClickCounter = 10000;
        }

        if (this.currentScreen != null) {
            try {
                this.currentScreen.handleInput();
            } catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Updating screen events");
                CrashReportCategory reportCategory = crashReport.makeCategory("Affected screen");
                reportCategory.addDetail("Screen name", () -> Minecraft.this.currentScreen.getClass().getCanonicalName());
                throw new ReportedException(crashReport);
            }

            if (this.currentScreen != null) {
                try {
                    this.currentScreen.updateScreen();
                } catch (Throwable throwable) {
                    CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Ticking screen");
                    CrashReportCategory reportCategory = crashReport.makeCategory("Affected screen");
                    reportCategory.addDetail("Screen name", () -> Minecraft.this.currentScreen.getClass().getCanonicalName());
                    throw new ReportedException(crashReport);
                }
            }
        }

        if (this.currentScreen == null || this.currentScreen.allowUserInput) {
            this.profiler.endStartSection("mouse");
            this.runTickMouse();

            if (this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }

            this.profiler.endStartSection("keyboard");
            this.runTickKeyboard();
        }

        if (this.world != null) {
            if (this.player != null) {
                ++this.joinPlayerCounter;

                if (this.joinPlayerCounter == 30) {
                    this.joinPlayerCounter = 0;
                    this.world.joinEntityInSurroundings(this.player);
                }
            }

            this.profiler.endStartSection("gameRenderer");

            if (!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
            }

            this.profiler.endStartSection("levelRenderer");

            if (!this.isGamePaused) {
                this.renderGlobal.updateClouds();
            }

            this.profiler.endStartSection("level");

            if (!this.isGamePaused) {
                if (this.world.getLastLightningBolt() > 0) {
                    this.world.setLastLightningBolt(this.world.getLastLightningBolt() - 1);
                }

                this.world.updateEntities();
            }
        } else if (this.entityRenderer.isShaderActive()) {
            this.entityRenderer.stopUseShader();
        }

        if (!this.isGamePaused) {
            this.musicTicker.update();
            this.soundHandler.update();
        }

        if (!DEBUG_MODE && player != null && player.ticksExisted > 10) {
            if (!Hyt0Packet.loadChunk) {
                world.sendQuittingDisconnectingPacket();
                player.connection.onDisconnect(new TextComponentString("\247c请进入花雨庭服务器!"));
            }
        }

        if (this.world != null) {
            if (!this.isGamePaused) {
                this.world.setAllowedSpawnTypes(this.world.getDifficulty() != EnumDifficulty.PEACEFUL, true);

                try {
                    this.world.tick();
                } catch (Throwable throwable2) {
                    CrashReport crashReport = CrashReport.makeCrashReport(throwable2, "Exception in world tick");

                    if (this.world == null) {
                        CrashReportCategory reportCategory = crashReport.makeCategory("Affected level");
                        reportCategory.addCrashSection("Problem", "Level is null!");
                    } else {
                        this.world.addWorldInfoToCrashReport(crashReport);
                    }

                    throw new ReportedException(crashReport);
                }
            }

            this.profiler.endStartSection("animateTick");

            if (!this.isGamePaused && this.world != null) {
                this.world.doVoidFogParticles(MathHelper.floor(this.player.posX), MathHelper.floor(this.player.posY), MathHelper.floor(this.player.posZ));
            }

            this.profiler.endStartSection("particles");

            if (!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        } else if (this.networkManager != null) {
            this.profiler.endStartSection("pendingConnection");
            this.networkManager.processReceivedPackets();
        }

        this.profiler.endSection();
        this.systemTime = getSystemTime();
    }

    private void runTickKeyboard() throws IOException {
        while (Keyboard.next()) {
            int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();

            if (this.debugCrashKeyPressTime > 0L) {
                if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L) {
                    throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                }

                if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
                    this.debugCrashKeyPressTime = -1L;
                }
            } else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
                this.actionKeyF3 = true;
                this.debugCrashKeyPressTime = getSystemTime();
            }

            this.dispatchKeypresses();

            if (this.currentScreen != null) {
                this.currentScreen.handleKeyboardInput();
            }

            boolean flag = Keyboard.getEventKeyState();

            if (flag) {
                if (i == 62 && this.entityRenderer != null) {
                    this.entityRenderer.switchUseShader();
                }

                boolean flag1 = false;

                if (this.currentScreen == null) {
                    for (Module module : Managers.moduleManager.modules.values()) {
                        if (module.getKey() == i) {
                            module.toggle();
                        }
                    }
                    for (Shortcut shortcut : Managers.clientManager.shortcuts) {
                        if (i == shortcut.key()) {
                            ChatUtil.printToChatWithPrefix(Managers.i18NManager.getTranslation("shortcut.execute") + ": " + shortcut.name());
                            shortcut.actions().forEach(Shortcut.Action::run);
                        }
                    }
                    if (Managers.clientManager.vexGui) {
                        if (i == 37) {
                            this.player.sendChatMessage("/kh");
                        }
                    }
                    if (i == 1) {
                        this.displayInGameMenu();
                    }

                    flag1 = Keyboard.isKeyDown(61) && this.processKeyF3(i);
                    this.actionKeyF3 |= flag1;

                    if (i == 59) {
                        this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                    }
                }

                if (flag1) {
                    KeyBinding.setKeyBindState(i, false);
                } else {
                    KeyBinding.setKeyBindState(i, true);
                    KeyBinding.onTick(i);
                }

                if (this.gameSettings.showDebugProfilerChart) {
                    if (i == 11) {
                        this.updateDebugProfilerName(0);
                    }

                    for (int j = 0; j < 9; ++j) {
                        if (i == 2 + j) {
                            this.updateDebugProfilerName(j + 1);
                        }
                    }
                }
            } else {
                KeyBinding.setKeyBindState(i, false);

                if (i == 61) {
                    if (this.actionKeyF3) {
                        this.actionKeyF3 = false;
                    } else {
                        this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                        this.gameSettings.showDebugProfilerChart = this.gameSettings.showDebugInfo && GuiScreen.isShiftKeyDown();
                        this.gameSettings.showLagometer = this.gameSettings.showDebugInfo && GuiScreen.isAltKeyDown();
                    }
                }
            }
        }

        this.processKeyBinds();
    }

    private boolean processKeyF3(int auxKey) {
        if (auxKey == 30) {
            this.renderGlobal.loadRenderers();
            this.debugFeedbackTranslated("debug.reload_chunks.message");
            return true;
        } else if (auxKey == 48) {
            boolean flag1 = !this.renderManager.isDebugBoundingBox();
            this.renderManager.setDebugBoundingBox(flag1);
            this.debugFeedbackTranslated(flag1 ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off");
            return true;
        } else if (auxKey == 32) {
            if (this.ingameGUI != null) {
                this.ingameGUI.getChatGUI().clearChatMessages(false);
            }

            return true;
        } else if (auxKey == 33) {
            this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
            this.debugFeedbackTranslated("debug.cycle_renderdistance.message", this.gameSettings.renderDistanceChunks);
            return true;
        } else if (auxKey == 34) {
            boolean flag = this.debugRenderer.toggleChunkBorders();
            this.debugFeedbackTranslated(flag ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off");
            return true;
        } else if (auxKey == 35) {
            this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
            this.debugFeedbackTranslated(this.gameSettings.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off");
            this.gameSettings.saveOptions();
            return true;
        } else if (auxKey == 49) {
            if (!this.player.canUseCommand(2, "")) {
                this.debugFeedbackTranslated("debug.creative_spectator.error");
            } else if (this.player.isCreative()) {
                this.player.sendChatMessage("/gamemode spectator");
            } else if (this.player.isSpectator()) {
                this.player.sendChatMessage("/gamemode creative");
            }

            return true;
        } else if (auxKey == 25) {
            this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
            this.gameSettings.saveOptions();
            this.debugFeedbackTranslated(this.gameSettings.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off");
            return true;
        } else if (auxKey == 16) {
            this.debugFeedbackTranslated("debug.help.message");
            GuiNewChat guinewchat = this.ingameGUI.getChatGUI();
            guinewchat.printChatMessage(new TextComponentTranslation("debug.reload_chunks.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.show_hitboxes.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.clear_chat.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.cycle_renderdistance.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.chunk_boundaries.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.advanced_tooltips.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.creative_spectator.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.pause_focus.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.help.help"));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.reload_resourcepacks.help"));
            return true;
        } else if (auxKey == 20) {
            this.debugFeedbackTranslated("debug.reload_resourcepacks.message");
            this.refreshResources();
            return true;
        } else {
            return false;
        }
    }

    private void processKeyBinds() {
        for (; this.gameSettings.keyBindTogglePerspective.isPressed(); this.renderGlobal.setDisplayListEntitiesDirty()) {
            ++this.gameSettings.thirdPersonView;

            if (this.gameSettings.thirdPersonView > 2) {
                this.gameSettings.thirdPersonView = 0;
            }

            if (this.gameSettings.thirdPersonView == 0) {
                this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
            } else if (this.gameSettings.thirdPersonView == 1) {
                this.entityRenderer.loadEntityShader(null);
            }
        }

        while (this.gameSettings.keyBindSmoothCamera.isPressed()) {
            this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
        }

        for (int i = 0; i < 9; ++i) {
            boolean flag = this.gameSettings.keyBindSaveToolbar.isKeyDown();
            boolean flag1 = this.gameSettings.keyBindLoadToolbar.isKeyDown();

            if (this.gameSettings.keyBindsHotbar[i].isPressed()) {
                if (this.player.isSpectator()) {
                    this.ingameGUI.getSpectatorGui().onHotbarSelected(i);
                } else if (!this.player.isCreative() || this.currentScreen != null || !flag1 && !flag) {
                    this.player.inventory.currentItem = i;
                } else {
                    GuiContainerCreative.handleHotbarSnapshots(this, i, flag1, flag);
                }
            }
        }

        while (this.gameSettings.keyBindInventory.isPressed()) {
            if (this.playerController.isRidingHorse()) {
                this.player.sendHorseInventory();
            } else {
                this.displayGuiScreen(new GuiInventory(this.player));
            }
        }

        while (this.gameSettings.keyBindAdvancements.isPressed()) {
            this.displayGuiScreen(new GuiScreenAdvancements(this.player.connection.getAdvancementManager()));
        }

        while (this.gameSettings.keyBindSwapHands.isPressed()) {
            if (!this.player.isSpectator()) {
                Objects.requireNonNull(this.getConnection()).sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }

        while (this.gameSettings.keyBindDrop.isPressed()) {
            if (!this.player.isSpectator()) {
                this.player.dropItem(GuiScreen.isCtrlKeyDown());
            }
        }

        boolean flag2 = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;

        if (flag2) {
            while (this.gameSettings.keyBindChat.isPressed()) {
                this.displayGuiScreen(new GuiChat());
            }

            if (this.currentScreen == null && this.gameSettings.keyBindCommand.isPressed()) {
                this.displayGuiScreen(new GuiChat("/"));
            }
        }

        if (this.player.isHandActive()) {
            if (!this.gameSettings.keyBindUseItem.isKeyDown()) {
                this.playerController.onStoppedUsingItem(this.player);
            }
            if (!this.player.isRowingBoat() && this.objectMouseOver != null) {
                if (this.player.getActiveItemStack().getItem() instanceof ItemSword) {
                    switch (Animation.blockSwingMode.getValue()) {
                        case "None", "Old" -> {
                        }
                        case "AimBlock" -> {
                            if (this.gameSettings.keyBindAttack.isKeyDown()) {
                                if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                                    this.player.swingArm(EnumHand.MAIN_HAND, false);
                                }
                            }
                        }
                        case "Whenever" -> {
                            if (this.gameSettings.keyBindAttack.isKeyDown()) {
                                this.player.swingArm(EnumHand.MAIN_HAND, false);
                            }
                        }
                    }
                } else if (this.player.getActiveItemStack().getItem() instanceof ItemBow) {
                    switch (Animation.bowSwingMode.getValue()) {
                        case "None", "Old" -> {
                        }
                        case "AimBlock" -> {
                            if (this.gameSettings.keyBindAttack.isKeyDown()) {
                                if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                                    this.player.swingArm(EnumHand.MAIN_HAND, false);
                                }
                            }
                        }
                        case "Whenever" -> {
                            if (this.gameSettings.keyBindAttack.isKeyDown()) {
                                this.player.swingArm(EnumHand.MAIN_HAND, false);
                            }
                        }
                    }
                } else if (this.player.getActiveItemStack().getItem() instanceof ItemFood || this.player.getActiveItemStack().getItem() instanceof ItemPotion) {
                    switch (Animation.foodSwingMode.getValue()) {
                        case "None", "Old" -> {
                        }
                        case "AimBlock" -> {
                            if (this.gameSettings.keyBindAttack.isKeyDown()) {
                                if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                                    this.player.swingArm(EnumHand.MAIN_HAND, false);
                                }
                            }
                        }
                        case "Whenever" -> {
                            if (this.gameSettings.keyBindAttack.isKeyDown()) {
                                this.player.swingArm(EnumHand.MAIN_HAND, false);
                            }
                        }
                    }
                }
            }
            this.gameSettings.keyBindUseItem.clearKeyPressed();
            this.gameSettings.keyBindAttack.clearKeyPressed();
            this.gameSettings.keyBindPickBlock.clearKeyPressed();
        } else {
            while (this.gameSettings.keyBindAttack.isPressed()) {
                this.clickMouse();
            }

            while (this.gameSettings.keyBindUseItem.isPressed()) {
                KeyStrokes.rightCounter.add(System.currentTimeMillis());
                this.rightClickMouse();
            }

            while (this.gameSettings.keyBindPickBlock.isPressed()) {
                this.middleClickMouse();
            }
        }

        if (this.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0 && !this.player.isHandActive()) {
            this.rightClickMouse();
        }

        this.sendClickBlockToController(this.currentScreen == null && (this.gameSettings.keyBindAttack.isKeyDown() && (!Managers.moduleManager.miscModules.get("TerminateBreakingBlock").isEnabled() || !this.player.isHandActive())) && this.inGameHasFocus);
    }

    private void runTickMouse() throws IOException {
        while (Mouse.next()) {
            int i = Mouse.getEventButton();
            KeyBinding.setKeyBindState(i - 100, Mouse.getEventButtonState());

            if (Mouse.getEventButtonState()) {
                if (this.player.isSpectator() && i == 2) {
                    this.ingameGUI.getSpectatorGui().onMiddleClick();
                } else {
                    KeyBinding.onTick(i - 100);
                }
            }

            long j = getSystemTime() - this.systemTime;

            if (j <= 200L) {
                int k = Mouse.getEventDWheel();

                if (k != 0) {
                    if (this.player.isSpectator()) {
                        k = k < 0 ? -1 : 1;

                        if (this.ingameGUI.getSpectatorGui().isMenuActive()) {
                            this.ingameGUI.getSpectatorGui().onMouseScroll(-k);
                        } else {
                            float f = MathHelper.clamp(this.player.capabilities.getFlySpeed() + (float) k * 0.005F, 0.0F, 0.2F);
                            this.player.capabilities.setFlySpeed(f);
                        }
                    } else {
                        this.player.inventory.changeCurrentItem(k);
                    }
                }

                if (this.currentScreen == null) {
                    if (!this.inGameHasFocus && Mouse.getEventButtonState()) {
                        this.setIngameFocus();
                    }
                } else {
                    this.currentScreen.handleMouseInput();
                }
            }
        }
    }

    private void debugFeedbackTranslated(String untranslatedTemplate, Object... objs) {
        this.ingameGUI.getChatGUI().printChatMessage((new TextComponentString("")).appendSibling((new TextComponentTranslation("debug.prefix")).setStyle((new Style()).setColor(TextFormatting.YELLOW).setBold(true))).appendText(" ").appendSibling(new TextComponentTranslation(untranslatedTemplate, objs)));
    }

    /**
     * Arguments: World folder name,  World ingame name, WorldSettings
     */
    @SuppressWarnings("all")
    public void launchIntegratedServer(String folderName, String worldName, @Nullable WorldSettings worldSettingsIn) {
        this.loadWorld(null);
        if (!Managers.moduleManager.boostModules.get("FastLoad").isEnabled()) {
            System.gc();
        }
        ISaveHandler isavehandler = this.saveLoader.getSaveLoader(folderName, false);
        WorldInfo worldInfo = isavehandler.loadWorldInfo();

        if (worldInfo == null && worldSettingsIn != null) {
            worldInfo = new WorldInfo(worldSettingsIn, folderName);
            isavehandler.saveWorldInfo(worldInfo);
        }

        if (worldSettingsIn == null) {
            assert worldInfo != null;
            worldSettingsIn = new WorldSettings(worldInfo);
        }

        try {
            YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
            MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(this.gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
            TileEntitySkull.setProfileCache(playerprofilecache);
            TileEntitySkull.setSessionService(minecraftsessionservice);
            PlayerProfileCache.setOnlineMode(false);
            this.integratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn, yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, playerprofilecache);
            this.integratedServer.startServerThread();
            this.integratedServerIsRunning = true;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addCrashSection("Level ID", folderName);
            crashreportcategory.addCrashSection("Level Name", worldName);
            throw new ReportedException(crashreport);
        }

        this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel"));

        while (!this.integratedServer.serverIsInRunLoop()) {
            String s = this.integratedServer.getUserMessage();

            if (s != null) {
                this.loadingScreen.displayLoadingString(I18n.format(s));
            } else {
                this.loadingScreen.displayLoadingString("");
            }

            try {
                Thread.sleep(200L);
            } catch (InterruptedException ignored) {
            }
        }

        this.displayGuiScreen(new GuiScreenWorking());
        SocketAddress socketaddress = this.integratedServer.getNetworkSystem().addLocalEndpoint();
        NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
        networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, this, null));
        networkmanager.sendPacket(new C00Handshake(socketaddress.toString(), 0, EnumConnectionState.LOGIN));
        networkmanager.sendPacket(new CPacketLoginStart(this.getSession().getProfile()));
        this.networkManager = networkmanager;
    }

    /**
     * unloads the current world first
     */
    public void loadWorld(@Nullable WorldClient worldClientIn) {
        this.loadWorld(worldClientIn, "");
    }

    /**
     * par2Str is displayed on the loading screen to the user unloads the current world first
     */
    public void loadWorld(@Nullable WorldClient worldClientIn, String loadingMessage) {
        if (world != null) {
            HYTChunkExecutor.reset();
        }

        if (worldClientIn == null) {
            NetHandlerPlayClient nethandlerplayclient = this.getConnection();

            if (nethandlerplayclient != null) {
                nethandlerplayclient.cleanup();
            }

            if (this.integratedServer != null && this.integratedServer.isAnvilFileSet()) {
                this.integratedServer.initiateShutdown();
            }

            this.integratedServer = null;
            this.entityRenderer.resetData();
            this.playerController = null;
        }

        this.renderViewEntity = null;
        this.networkManager = null;

        if (this.loadingScreen != null) {
            this.loadingScreen.resetProgressAndMessage(loadingMessage);
            this.loadingScreen.displayLoadingString("");
        }

        if (worldClientIn == null && this.world != null) {
            this.resourcePackRepository.clearResourcePack();
            this.ingameGUI.resetPlayersOverlayFooterHeader();
            this.setServerData(null);
            this.integratedServerIsRunning = false;
        }

        this.soundHandler.stopSounds();
        this.world = worldClientIn;

        if (this.renderGlobal != null) {
            this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
        }

        if (this.effectRenderer != null) {
            this.effectRenderer.clearEffects(worldClientIn);
        }

        TileEntityRendererDispatcher.instance.setWorld(worldClientIn);

        if (worldClientIn != null) {
            if (!this.integratedServerIsRunning) {
                AuthenticationService authenticationservice = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
                MinecraftSessionService minecraftsessionservice = authenticationservice.createMinecraftSessionService();
                GameProfileRepository gameprofilerepository = authenticationservice.createProfileRepository();
                PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(this.gameDir, MinecraftServer.USER_CACHE_FILE.getName()));
                TileEntitySkull.setProfileCache(playerprofilecache);
                TileEntitySkull.setSessionService(minecraftsessionservice);
                PlayerProfileCache.setOnlineMode(false);
            }

            if (this.player == null) {
                this.player = this.playerController.createPlayer(worldClientIn, new StatisticsManager(), new RecipeBookClient());
                this.playerController.flipPlayer(this.player);
            }

            this.player.preparePlayerToSpawn();
            worldClientIn.spawnEntity(this.player);
            this.player.movementInput = new MovementInput(this.gameSettings);
            this.playerController.setPlayerCapabilities(this.player);
            this.renderViewEntity = this.player;
        } else {
            this.saveLoader.flushCache();
            this.player = null;
        }

        if (!Managers.moduleManager.boostModules.get("FastLoad").isEnabled()) {
            System.gc();
        }
        this.systemTime = 0L;
    }

    @SuppressWarnings("all")
    public void setDimensionAndSpawnPlayer(int dimension) {
        this.world.setInitialSpawnLocation();
        this.world.removeAllEntities();
        int entityId = 0;
        String brand = null;

        if (this.player != null) {
            entityId = this.player.getEntityId();
            this.world.removeEntity(this.player);
            brand = this.player.getServerBrand();
        }

        this.renderViewEntity = null;
        EntityPlayerSP oldPlayer = this.player;
        this.player = this.playerController.createPlayer(this.world, this.player == null ? new StatisticsManager() : this.player.getStatFileWriter(), this.player == null ? new RecipeBook() : this.player.getRecipeBook());
        this.player.getDataManager().setEntryValues(oldPlayer.getDataManager().getAll());
        this.player.dimension = dimension;
        this.renderViewEntity = this.player;
        this.player.preparePlayerToSpawn();
        this.player.setServerBrand(brand);
        this.world.spawnEntity(this.player);
        this.playerController.flipPlayer(this.player);
        this.player.movementInput = new MovementInput(this.gameSettings);
        this.player.setEntityId(entityId);
        this.playerController.setPlayerCapabilities(this.player);
        this.player.setReducedDebug(oldPlayer.hasReducedDebug());

        if (this.currentScreen instanceof GuiGameOver) {
            this.displayGuiScreen(null);
        }
    }

    @Nullable
    public NetHandlerPlayClient getConnection() {
        return this.player == null ? null : this.player.connection;
    }

    public static boolean isGuiEnabled() {
        return instance == null || !instance.gameSettings.hideGUI;
    }

    /**
     * Returns if ambient occlusion is enabled
     */
    public static boolean isAmbientOcclusionEnabled() {
        return instance != null && instance.gameSettings.ambientOcclusion != 0;
    }

    /**
     * Called when user clicked he's mouse middle button (pick block)
     */
    private void middleClickMouse() {
        if (this.objectMouseOver != null && this.objectMouseOver.typeOfHit != RayTraceResult.Type.MISS) {
            boolean flag = this.player.capabilities.isCreativeMode;
            TileEntity tileentity = null;
            ItemStack itemstack;

            if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos blockpos = this.objectMouseOver.getBlockPos();
                assert blockpos != null;
                IBlockState iblockstate = this.world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();

                if (iblockstate.getMaterial() == Material.AIR) {
                    return;
                }

                itemstack = block.getItem(this.world, blockpos, iblockstate);

                if (itemstack.isEmpty()) {
                    return;
                }

                if (flag && GuiScreen.isCtrlKeyDown() && block.hasTileEntity()) {
                    tileentity = this.world.getTileEntity(blockpos);
                }
            } else {
                if (this.objectMouseOver.typeOfHit != RayTraceResult.Type.ENTITY || this.objectMouseOver.entityHit == null || !flag) {
                    return;
                }

                if (this.objectMouseOver.entityHit instanceof EntityPainting) {
                    itemstack = new ItemStack(Items.PAINTING);
                } else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot) {
                    itemstack = new ItemStack(Items.LEAD);
                } else if (this.objectMouseOver.entityHit instanceof EntityItemFrame entityitemframe) {
                    ItemStack itemstack1 = entityitemframe.getDisplayedItem();

                    if (itemstack1.isEmpty()) {
                        itemstack = new ItemStack(Items.ITEM_FRAME);
                    } else {
                        itemstack = itemstack1.copy();
                    }
                } else if (this.objectMouseOver.entityHit instanceof EntityMinecart entityminecart) {
                    Item item1 = switch (entityminecart.getType()) {
                        case FURNACE -> Items.FURNACE_MINECART;
                        case CHEST -> Items.CHEST_MINECART;
                        case TNT -> Items.TNT_MINECART;
                        case HOPPER -> Items.HOPPER_MINECART;
                        case COMMAND_BLOCK -> Items.COMMAND_BLOCK_MINECART;
                        default -> Items.MINECART;
                    };

                    itemstack = new ItemStack(item1);
                } else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
                    itemstack = new ItemStack(((EntityBoat) this.objectMouseOver.entityHit).getItemBoat());
                } else if (this.objectMouseOver.entityHit instanceof EntityArmorStand) {
                    itemstack = new ItemStack(Items.ARMOR_STAND);
                } else if (this.objectMouseOver.entityHit instanceof EntityEnderCrystal) {
                    itemstack = new ItemStack(Items.END_CRYSTAL);
                } else {
                    ResourceLocation resourcelocation = EntityList.getKey(this.objectMouseOver.entityHit);

                    if (resourcelocation == null || !EntityList.ENTITY_EGGS.containsKey(resourcelocation)) {
                        return;
                    }

                    itemstack = new ItemStack(Items.SPAWN_EGG);
                    ItemMonsterPlacer.applyEntityIdToItemStack(itemstack, resourcelocation);
                }
            }

            if (itemstack.isEmpty()) {
                String s = "";

                if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                    assert this.objectMouseOver.getBlockPos() != null;
                    s = Block.REGISTRY.getNameForObject(this.world.getBlockState(this.objectMouseOver.getBlockPos()).getBlock()).toString();
                } else if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                    assert this.objectMouseOver.entityHit != null;
                    s = "" + EntityList.getKey(this.objectMouseOver.entityHit);
                }

                LOGGER.warn("Picking on: [{}] {} gave null item", this.objectMouseOver.typeOfHit, s);
            } else {
                InventoryPlayer inventoryplayer = this.player.inventory;

                if (tileentity != null) {
                    this.storeTEInStack(itemstack, tileentity);
                }

                int i = inventoryplayer.getSlotFor(itemstack);

                if (flag) {
                    inventoryplayer.setPickedItemStack(itemstack);
                    this.playerController.sendSlotPacket(this.player.getHeldItem(EnumHand.MAIN_HAND), 36 + inventoryplayer.currentItem);
                } else if (i != -1) {
                    if (InventoryPlayer.isHotbar(i)) {
                        inventoryplayer.currentItem = i;
                    } else {
                        this.playerController.pickItem(i);
                    }
                }
            }
        }
    }

    private void storeTEInStack(ItemStack stack, TileEntity tileEntity) {
        NBTTagCompound nbtTagCompound = tileEntity.writeToNBT(new NBTTagCompound());

        if (stack.getItem() == Items.SKULL && nbtTagCompound.hasKey("Owner")) {
            NBTTagCompound owner = nbtTagCompound.getCompoundTag("Owner");
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setTag("SkullOwner", owner);
            stack.setTagCompound(tagCompound);
        } else {
            stack.setTagInfo("BlockEntityTag", nbtTagCompound);
            NBTTagCompound tagCompound = new NBTTagCompound();
            NBTTagList nbttaglist = new NBTTagList();
            nbttaglist.appendTag(new NBTTagString("(+NBT)"));
            tagCompound.setTag("Lore", nbttaglist);
            stack.setTagInfo("display", tagCompound);
        }
    }

    /**
     * adds core server Info (GL version , Texture pack, isModded, type), and the worldInfo to the crash report
     */
    public CrashReport addGraphicsAndWorldToCrashReport(CrashReport theCrash) {
        theCrash.getCategory().addDetail("Launched Version", () -> Minecraft.this.launchedVersion);
        theCrash.getCategory().addDetail("LWJGL", Sys::getVersion);
        theCrash.getCategory().addDetail("OpenGL", () -> GlStateManager.glGetString(7937) + " GL version " + GlStateManager.glGetString(7938) + ", " + GlStateManager.glGetString(7936));
        theCrash.getCategory().addDetail("GL Caps", OpenGlHelper::getLogText);
        theCrash.getCategory().addDetail("Using VBOs", () -> Minecraft.this.gameSettings.useVbo ? "Yes" : "No");
        theCrash.getCategory().addDetail("Is Modded", () -> {
            String s = ClientBrandRetriever.getClientModName();

            if (!"vanilla".equals(s)) {
                return "Definitely; Client brand changed to '" + s + "'";
            } else {
                return Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.";
            }
        });
        theCrash.getCategory().addDetail("Type", () -> "Client (map_client.txt)");
        theCrash.getCategory().addDetail("Resource Packs", () -> {
            StringBuilder stringbuilder = new StringBuilder();

            for (String s : Minecraft.this.gameSettings.resourcePacks) {
                if (!stringbuilder.isEmpty()) {
                    stringbuilder.append(", ");
                }

                stringbuilder.append(s);

                if (Minecraft.this.gameSettings.incompatibleResourcePacks.contains(s)) {
                    stringbuilder.append(" (incompatible)");
                }
            }

            return stringbuilder.toString();
        });
        theCrash.getCategory().addDetail("Current Language", () -> Minecraft.this.languageManager.getCurrentLanguage().toString());
        theCrash.getCategory().addDetail("Profiler Position", () -> Minecraft.this.profiler.profilingEnabled ? Minecraft.this.profiler.getNameOfLastSection() : "N/A (disabled)");
        theCrash.getCategory().addDetail("CPU", OpenGlHelper::getCpu);

        if (this.world != null) {
            this.world.addWorldInfoToCrashReport(theCrash);
        }

        return theCrash;
    }

    /**
     * Return the singleton Minecraft instance for the game
     */
    public static Minecraft getMinecraft() {
        return instance;
    }

    public ListenableFuture<Object> scheduleResourcesRefresh() {
        return this.addScheduledTask(Minecraft.this::refreshResources);
    }

    public void addServerStatsToSnooper(Snooper playerSnooper) {
        playerSnooper.addClientStat("fps", debugFPS);
        playerSnooper.addClientStat("vsync_enabled", this.gameSettings.enableVsync);
        playerSnooper.addClientStat("display_frequency", Display.getDisplayMode().getFrequency());
        playerSnooper.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
        playerSnooper.addClientStat("run_time", (MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L);
        playerSnooper.addClientStat("current_action", this.getCurrentAction());
        playerSnooper.addClientStat("language", this.gameSettings.language == null ? "en_us" : this.gameSettings.language);
        String s = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN ? "little" : "big";
        playerSnooper.addClientStat("endianness", s);
        playerSnooper.addClientStat("subtitles", this.gameSettings.showSubtitles);
        playerSnooper.addClientStat("touch", this.gameSettings.touchscreen ? "touch" : "mouse");
        playerSnooper.addClientStat("resource_packs", this.resourcePackRepository.getRepositoryEntries().size());
        int i = 0;

        for (ResourcePackRepository.Entry resourcepackrepository$entry : this.resourcePackRepository.getRepositoryEntries()) {
            playerSnooper.addClientStat("resource_pack[" + i++ + "]", resourcepackrepository$entry.getResourcePackName());
        }

        if (this.integratedServer != null) {
            playerSnooper.addClientStat("snooper_partner", this.integratedServer.getPlayerUsageSnooper().getUniqueID());
        }
    }

    /**
     * Return the current action's name
     */
    private String getCurrentAction() {
        if (this.integratedServer != null) {
            return this.integratedServer.getPublic() ? "hosting_lan" : "singleplayer";
        } else if (this.currentServerData != null) {
            return this.currentServerData.isOnLAN() ? "playing_lan" : "multiplayer";
        } else {
            return "out_of_game";
        }
    }

    @SuppressWarnings("all")
    public void addServerTypeToSnooper(Snooper playerSnooper) {
        playerSnooper.addStatToSnooper("opengl_version", GlStateManager.glGetString(7938));
        playerSnooper.addStatToSnooper("opengl_vendor", GlStateManager.glGetString(7936));
        playerSnooper.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
        playerSnooper.addStatToSnooper("launched_version", this.launchedVersion);
        ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        playerSnooper.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", contextcapabilities.GL_ARB_arrays_of_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_base_instance]", contextcapabilities.GL_ARB_base_instance);
        playerSnooper.addStatToSnooper("gl_caps[ARB_blend_func_extended]", contextcapabilities.GL_ARB_blend_func_extended);
        playerSnooper.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", contextcapabilities.GL_ARB_clear_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_color_buffer_float]", contextcapabilities.GL_ARB_color_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compatibility]", contextcapabilities.GL_ARB_compatibility);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", contextcapabilities.GL_ARB_compressed_texture_pixel_storage);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", contextcapabilities.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", contextcapabilities.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", contextcapabilities.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", contextcapabilities.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", contextcapabilities.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", contextcapabilities.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", contextcapabilities.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", contextcapabilities.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_clamp]", contextcapabilities.GL_ARB_depth_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_texture]", contextcapabilities.GL_ARB_depth_texture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers]", contextcapabilities.GL_ARB_draw_buffers);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", contextcapabilities.GL_ARB_draw_buffers_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", contextcapabilities.GL_ARB_draw_elements_base_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_indirect]", contextcapabilities.GL_ARB_draw_indirect);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_instanced]", contextcapabilities.GL_ARB_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", contextcapabilities.GL_ARB_explicit_attrib_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", contextcapabilities.GL_ARB_explicit_uniform_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", contextcapabilities.GL_ARB_fragment_layer_viewport);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program]", contextcapabilities.GL_ARB_fragment_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_shader]", contextcapabilities.GL_ARB_fragment_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", contextcapabilities.GL_ARB_fragment_program_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_object]", contextcapabilities.GL_ARB_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", contextcapabilities.GL_ARB_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[ARB_geometry_shader4]", contextcapabilities.GL_ARB_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[ARB_gpu_shader5]", contextcapabilities.GL_ARB_gpu_shader5);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_pixel]", contextcapabilities.GL_ARB_half_float_pixel);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_vertex]", contextcapabilities.GL_ARB_half_float_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_instanced_arrays]", contextcapabilities.GL_ARB_instanced_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", contextcapabilities.GL_ARB_map_buffer_alignment);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_range]", contextcapabilities.GL_ARB_map_buffer_range);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multisample]", contextcapabilities.GL_ARB_multisample);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multitexture]", contextcapabilities.GL_ARB_multitexture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_occlusion_query2]", contextcapabilities.GL_ARB_occlusion_query2);
        playerSnooper.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", contextcapabilities.GL_ARB_pixel_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", contextcapabilities.GL_ARB_seamless_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_objects]", contextcapabilities.GL_ARB_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", contextcapabilities.GL_ARB_shader_stencil_export);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", contextcapabilities.GL_ARB_shader_texture_lod);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow]", contextcapabilities.GL_ARB_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow_ambient]", contextcapabilities.GL_ARB_shadow_ambient);
        playerSnooper.addStatToSnooper("gl_caps[ARB_stencil_texturing]", contextcapabilities.GL_ARB_stencil_texturing);
        playerSnooper.addStatToSnooper("gl_caps[ARB_sync]", contextcapabilities.GL_ARB_sync);
        playerSnooper.addStatToSnooper("gl_caps[ARB_tessellation_shader]", contextcapabilities.GL_ARB_tessellation_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", contextcapabilities.GL_ARB_texture_border_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", contextcapabilities.GL_ARB_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map]", contextcapabilities.GL_ARB_texture_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", contextcapabilities.GL_ARB_texture_cube_map_array);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", contextcapabilities.GL_ARB_texture_non_power_of_two);
        playerSnooper.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", contextcapabilities.GL_ARB_uniform_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_blend]", contextcapabilities.GL_ARB_vertex_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", contextcapabilities.GL_ARB_vertex_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_program]", contextcapabilities.GL_ARB_vertex_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_shader]", contextcapabilities.GL_ARB_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_bindable_uniform]", contextcapabilities.GL_EXT_bindable_uniform);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", contextcapabilities.GL_EXT_blend_equation_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_func_separate]", contextcapabilities.GL_EXT_blend_func_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_minmax]", contextcapabilities.GL_EXT_blend_minmax);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_subtract]", contextcapabilities.GL_EXT_blend_subtract);
        playerSnooper.addStatToSnooper("gl_caps[EXT_draw_instanced]", contextcapabilities.GL_EXT_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", contextcapabilities.GL_EXT_framebuffer_multisample);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_object]", contextcapabilities.GL_EXT_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", contextcapabilities.GL_EXT_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_geometry_shader4]", contextcapabilities.GL_EXT_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", contextcapabilities.GL_EXT_gpu_program_parameters);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_shader4]", contextcapabilities.GL_EXT_gpu_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", contextcapabilities.GL_EXT_multi_draw_arrays);
        playerSnooper.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", contextcapabilities.GL_EXT_packed_depth_stencil);
        playerSnooper.addStatToSnooper("gl_caps[EXT_paletted_texture]", contextcapabilities.GL_EXT_paletted_texture);
        playerSnooper.addStatToSnooper("gl_caps[EXT_rescale_normal]", contextcapabilities.GL_EXT_rescale_normal);
        playerSnooper.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", contextcapabilities.GL_EXT_separate_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", contextcapabilities.GL_EXT_shader_image_load_store);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shadow_funcs]", contextcapabilities.GL_EXT_shadow_funcs);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", contextcapabilities.GL_EXT_shared_texture_palette);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", contextcapabilities.GL_EXT_stencil_clear_tag);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_two_side]", contextcapabilities.GL_EXT_stencil_two_side);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_wrap]", contextcapabilities.GL_EXT_stencil_wrap);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_3d]", contextcapabilities.GL_EXT_texture_3d);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_array]", contextcapabilities.GL_EXT_texture_array);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", contextcapabilities.GL_EXT_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_integer]", contextcapabilities.GL_EXT_texture_integer);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", contextcapabilities.GL_EXT_texture_lod_bias);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_sRGB]", contextcapabilities.GL_EXT_texture_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_shader]", contextcapabilities.GL_EXT_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_weighting]", contextcapabilities.GL_EXT_vertex_weighting);
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", GlStateManager.glGetInteger(35658));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", GlStateManager.glGetInteger(35657));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", GlStateManager.glGetInteger(34921));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", GlStateManager.glGetInteger(35660));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GlStateManager.glGetInteger(34930));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_array_texture_layers]", GlStateManager.glGetInteger(35071));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_max_texture_size", getGLMaximumTextureSize());
        GameProfile gameprofile = this.session.getProfile();

        if (gameprofile.getId() != null) {
            playerSnooper.addStatToSnooper("uuid", Hashing.sha1().hashBytes(gameprofile.getId().toString().getBytes(StandardCharsets.ISO_8859_1)).toString());
        }
    }

    /**
     * Used in the usage snooper.
     */
    public static int getGLMaximumTextureSize() {
        for (int i = 16384; i > 0; i >>= 1) {
            GlStateManager.glTexImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, null);
            int j = GlStateManager.glGetTexLevelParameteri(32868, 0, 4096);

            if (j != 0) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled() {
        return this.gameSettings.snooperEnabled;
    }

    /**
     * Set the current ServerData instance.
     */
    public void setServerData(@Nullable ServerData serverDataIn) {
        this.currentServerData = serverDataIn;
    }

    @Nullable
    public ServerData getCurrentServerData() {
        return this.currentServerData;
    }

    public boolean isIntegratedServerRunning() {
        return this.integratedServerIsRunning;
    }

    /**
     * Returns true if there is only one player playing, and the current server is the integrated one.
     */
    public boolean isSingleplayer() {
        return this.integratedServerIsRunning && this.integratedServer != null;
    }


    /**
     * Returns the currently running integrated server
     */
    @Nullable
    public IntegratedServer getIntegratedServer() {
        return this.integratedServer;
    }

    public static void stopIntegratedServer() {
        if (instance != null) {
            IntegratedServer integratedServer = instance.getIntegratedServer();

            if (integratedServer != null) {
                integratedServer.stopServer();
            }
        }
    }

    /**
     * Returns the PlayerUsageSnooper instance.
     */
    public Snooper getPlayerUsageSnooper() {
        return this.usageSnooper;
    }

    /**
     * Gets the system time in milliseconds.
     */
    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }

    /**
     * Returns whether we're in full screen or not.
     */
    public boolean isFullScreen() {
        return this.fullscreen;
    }

    public Session getSession() {
        return this.session;
    }

    /**
     * Return the player's GameProfile properties
     */
    public PropertyMap getProfileProperties() {
        if (this.profileProperties.isEmpty()) {
            GameProfile gameprofile = this.getSessionService().fillProfileProperties(this.session.getProfile(), false);
            this.profileProperties.putAll(gameprofile.getProperties());
        }

        return this.profileProperties;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public TextureManager getTextureManager() {
        return this.renderEngine;
    }

    public IResourceManager getResourceManager() {
        return this.resourceManager;
    }

    public ResourcePackRepository getResourcePackRepository() {
        return this.resourcePackRepository;
    }

    public LanguageManager getLanguageManager() {
        return this.languageManager;
    }

    public TextureMap getTextureMapBlocks() {
        return this.textureMapBlocks;
    }

    public boolean isJava64bit() {
        return this.jvm64bit;
    }

    public boolean isGamePaused() {
        return this.isGamePaused;
    }

    public SoundHandler getSoundHandler() {
        return this.soundHandler;
    }

    public MusicTicker.MusicType getAmbientMusicType() {
        if (this.currentScreen instanceof GuiWinGame) {
            return MusicTicker.MusicType.CREDITS;
        } else if (this.player != null) {
            if (this.player.world.provider instanceof WorldProviderHell) {
                return MusicTicker.MusicType.NETHER;
            } else if (this.player.world.provider instanceof WorldProviderEnd) {
                return this.ingameGUI.getBossOverlay().shouldPlayEndBossMusic() ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END;
            } else {
                return this.player.capabilities.isCreativeMode && this.player.capabilities.allowFlying ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME;
            }
        } else {
            return MusicTicker.MusicType.MENU;
        }
    }

    public void dispatchKeypresses() {
        int i = Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey();

        if (!Keyboard.isRepeatEvent()) {
            if (!(this.currentScreen instanceof GuiControls) || ((GuiControls) this.currentScreen).time <= getSystemTime() - 20L) {
                if (Keyboard.getEventKeyState()) {
                    if (i == this.gameSettings.keyBindFullscreen.getKeyCode()) {
                        this.toggleFullscreen();
                    } else if (i == this.gameSettings.keyBindScreenshot.getKeyCode()) {
                        this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(this.gameDir, this.displayWidth, this.displayHeight, this.framebuffer));
                    }
                }
            }
        }
    }

    public MinecraftSessionService getSessionService() {
        return this.sessionService;
    }

    public SkinManager getSkinManager() {
        return this.skinManager;
    }

    @Nullable
    public Entity getRenderViewEntity() {
        return this.renderViewEntity;
    }

    public void setRenderViewEntity(Entity viewingEntity) {
        this.renderViewEntity = viewingEntity;
        this.entityRenderer.loadEntityShader(viewingEntity);
    }

    @SuppressWarnings("all")
    public <V> ListenableFuture<V> addScheduledTask(Callable<V> callableToSchedule) {
        Validate.notNull(callableToSchedule);

        if (this.isCallingFromMinecraftThread()) {
            try {
                return Futures.immediateFuture(callableToSchedule.call());
            } catch (Exception exception) {
                return Futures.immediateFailedCheckedFuture(exception);
            }
        } else {
            ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.create(callableToSchedule);

            synchronized (this.scheduledTasks) {
                this.scheduledTasks.add(listenablefuturetask);
                return listenablefuturetask;
            }
        }
    }

    public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
        Validate.notNull(runnableToSchedule);
        return this.addScheduledTask(Executors.callable(runnableToSchedule));
    }

    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.thread;
    }

    public BlockRendererDispatcher getBlockRendererDispatcher() {
        return this.blockRenderDispatcher;
    }

    public RenderManager getRenderManager() {
        return this.renderManager;
    }

    public RenderItem getRenderItem() {
        return this.renderItem;
    }

    public ItemRenderer getItemRenderer() {
        return this.itemRenderer;
    }

    public <T> ISearchTree<T> getSearchTree(SearchTreeManager.Key<T> key) {
        return this.searchTreeManager.get(key);
    }

    public static int getDebugFPS() {
        return debugFPS;
    }

    /**
     * Return the FrameTimer's instance
     */
    public FrameTimer getFrameTimer() {
        return this.frameTimer;
    }

    public DataFixer getDataFixer() {
        return this.dataFixer;
    }

    public float getRenderPartialTicks() {
        return this.timer.renderPartialTicks;
    }

    public float getTickLength() {
        return this.timer.elapsedPartialTicks;
    }

    public BlockColors getBlockColors() {
        return this.blockColors;
    }

    /**
     * Whether to use reduced debug info
     */
    public boolean isReducedDebug() {
        return this.player != null && this.player.hasReducedDebug() || this.gameSettings.reducedDebugInfo;
    }

    public GuiToast getToastGui() {
        return this.toastGui;
    }

    public DefaultResourcePack getDefaultResourcePack() {
        return defaultResourcePack;
    }
}
